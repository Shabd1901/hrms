package com.peopleops.service;

import com.peopleops.dto.PayrollGenerateRequestDTO;
import com.peopleops.dto.PayrollResponseDTO;
import com.peopleops.entity.Attendance;
import com.peopleops.entity.Employee;
import com.peopleops.entity.LeaveRequest;
import com.peopleops.entity.LeaveType;
import com.peopleops.entity.Payroll;
import com.peopleops.enums.AttendanceStatus;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.enums.LeaveRequestStatus;
import com.peopleops.enums.PayrollStatus;
import com.peopleops.repository.AttendanceRepository;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.LeaveRequestRepository;
import com.peopleops.repository.LeaveTypeRepository;
import com.peopleops.repository.PayrollRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    public PayrollService(
            PayrollRepository payrollRepository,
            EmployeeRepository employeeRepository,
            AttendanceRepository attendanceRepository,
            LeaveRequestRepository leaveRequestRepository,
            LeaveTypeRepository leaveTypeRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveTypeRepository = leaveTypeRepository;
    }

    public PayrollResponseDTO generatePayroll(PayrollGenerateRequestDTO request) {
        if (request.getEmployeeId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee id is required");
        }
        if (request.getPayMonth() == null || request.getPayMonth() < 1 || request.getPayMonth() > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid pay month (1-12) is required");
        }
        if (request.getPayYear() == null || request.getPayYear() < 2000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid pay year is required");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId()));

        if (employee.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot generate payroll for an exited employee");
        }

        if (payrollRepository.existsByEmployeeIdAndPayMonthAndPayYear(
                request.getEmployeeId(), request.getPayMonth(), request.getPayYear())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payroll already exists for this employee for month " + request.getPayMonth() + "/" + request.getPayYear());
        }

        LocalDate start = LocalDate.of(request.getPayYear(), request.getPayMonth(), 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        int workingDays = 0;
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            DayOfWeek dow = d.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                workingDays++;
            }
        }

        List<Attendance> attendances = attendanceRepository.findByEmployeeIdAndAttendanceDateBetween(request.getEmployeeId(), start, end);
        double absentCount = 0.0;
        double halfDayCount = 0.0;
        for (Attendance a : attendances) {
            if (a.getStatus() == AttendanceStatus.ABSENT) {
                absentCount += 1.0;
            } else if (a.getStatus() == AttendanceStatus.HALF_DAY) {
                halfDayCount += 1.0;
            }
        }

        double unpaidLeaveDays = 0.0;
        List<LeaveRequest> approvedLeaves = leaveRequestRepository.findByEmployeeIdAndStatus(request.getEmployeeId(), LeaveRequestStatus.APPROVED);
        for (LeaveRequest lr : approvedLeaves) {
            LeaveType leaveType = leaveTypeRepository.findById(lr.getLeaveTypeId()).orElse(null);
            if (leaveType != null && Boolean.FALSE.equals(leaveType.getPaid())) {
                LocalDate leaveStart = lr.getFromDate().isBefore(start) ? start : lr.getFromDate();
                LocalDate leaveEnd = lr.getToDate().isAfter(end) ? end : lr.getToDate();
                if (!leaveStart.isAfter(leaveEnd)) {
                    for (LocalDate ld = leaveStart; !ld.isAfter(leaveEnd); ld = ld.plusDays(1)) {
                        unpaidLeaveDays += 1.0;
                    }
                }
            }
        }

        double lopDays = absentCount + (0.5 * halfDayCount) + unpaidLeaveDays;
        BigDecimal gross = employee.getMonthlySalary();

        BigDecimal lopDeduction = (workingDays > 0 && lopDays > 0)
            ? gross.divide(BigDecimal.valueOf(workingDays), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(lopDays)).setScale(2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        BigDecimal pfDeduction = gross.multiply(BigDecimal.valueOf(0.12)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal netSalary = gross.subtract(lopDeduction).subtract(pfDeduction).setScale(2, RoundingMode.HALF_UP);

        Payroll payroll = new Payroll();
        payroll.setEmployeeId(request.getEmployeeId());
        payroll.setPayMonth(request.getPayMonth());
        payroll.setPayYear(request.getPayYear());
        payroll.setWorkingDays(workingDays);
        payroll.setLopDays(lopDays);
        payroll.setGrossSalary(gross);
        payroll.setLopDeduction(lopDeduction);
        payroll.setPfDeduction(pfDeduction);
        payroll.setNetSalary(netSalary);
        payroll.setStatus(PayrollStatus.GENERATED);
        payroll.setGeneratedOn(LocalDate.now());

        Payroll saved = payrollRepository.save(payroll);
        return toResponseDTO(saved);
    }

    public List<PayrollResponseDTO> getAllPayrolls(Long employeeId) {
        List<Payroll> list = (employeeId != null)
            ? payrollRepository.findByEmployeeId(employeeId)
            : payrollRepository.findAll();

        List<PayrollResponseDTO> responses = new ArrayList<>();
        for (Payroll p : list) {
            responses.add(toResponseDTO(p));
        }
        return responses;
    }

    public PayrollResponseDTO getPayrollById(Long id) {
        Payroll payroll = payrollRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll record not found with id: " + id));
        return toResponseDTO(payroll);
    }

    public void deletePayroll(Long id) {
        Payroll payroll = payrollRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll record not found with id: " + id));

        if (payroll.getStatus() != PayrollStatus.GENERATED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only GENERATED payroll records can be deleted");
        }

        payrollRepository.delete(payroll);
    }

    public PayrollResponseDTO payPayroll(Long id) {
        Payroll payroll = payrollRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll record not found with id: " + id));

        payroll.setStatus(PayrollStatus.PAID);
        Payroll updated = payrollRepository.save(payroll);
        return toResponseDTO(updated);
    }

    private PayrollResponseDTO toResponseDTO(Payroll entity) {
        return new PayrollResponseDTO(
            entity.getId(),
            entity.getEmployeeId(),
            entity.getPayMonth(),
            entity.getPayYear(),
            entity.getWorkingDays(),
            entity.getLopDays(),
            entity.getGrossSalary(),
            entity.getLopDeduction(),
            entity.getPfDeduction(),
            entity.getNetSalary(),
            entity.getStatus(),
            entity.getGeneratedOn()
        );
    }
}
