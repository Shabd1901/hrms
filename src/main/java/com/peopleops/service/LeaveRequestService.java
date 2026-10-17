package com.peopleops.service;

import com.peopleops.dto.LeaveRequestRequestDTO;
import com.peopleops.dto.LeaveRequestResponseDTO;
import com.peopleops.entity.Attendance;
import com.peopleops.entity.Employee;
import com.peopleops.entity.LeaveBalance;
import com.peopleops.entity.LeaveRequest;
import com.peopleops.entity.LeaveType;
import com.peopleops.enums.AttendanceStatus;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.enums.LeaveRequestStatus;
import com.peopleops.repository.AttendanceRepository;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.LeaveBalanceRepository;
import com.peopleops.repository.LeaveRequestRepository;
import com.peopleops.repository.LeaveTypeRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final AttendanceRepository attendanceRepository;

    public LeaveRequestService(
            LeaveRequestRepository leaveRequestRepository,
            EmployeeRepository employeeRepository,
            LeaveTypeRepository leaveTypeRepository,
            LeaveBalanceRepository leaveBalanceRepository,
            AttendanceRepository attendanceRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public LeaveRequestResponseDTO createLeaveRequest(LeaveRequestRequestDTO request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId()));

        if (employee.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only working employees can request leave");
        }

        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave type not found with id: " + request.getLeaveTypeId()));

        if (request.getFromDate().isAfter(request.getToDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromDate must be on or before toDate");
        }

        if (request.getFromDate().getYear() != request.getToDate().getYear()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leave request cannot span across multiple calendar years");
        }

        int days = (int) ChronoUnit.DAYS.between(request.getFromDate(), request.getToDate()) + 1;

        List<LeaveRequest> existing = leaveRequestRepository.findByEmployeeId(request.getEmployeeId());
        for (LeaveRequest lr : existing) {
            if (lr.getStatus() == LeaveRequestStatus.PENDING || lr.getStatus() == LeaveRequestStatus.APPROVED) {
                boolean overlap = !(lr.getToDate().isBefore(request.getFromDate()) || lr.getFromDate().isAfter(request.getToDate()));
                if (overlap) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leave request dates overlap with an existing pending or approved request");
                }
            }
        }

        if (Boolean.TRUE.equals(leaveType.getPaid())) {
            LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndBalanceYear(request.getEmployeeId(), request.getLeaveTypeId(), request.getFromDate().getYear())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No leave balance found for this leave type and year"));

            int available = balance.getTotalDays() - balance.getUsedDays();
            if (available < days) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient leave balance: available " + available + ", requested " + days);
            }
        }

        LeaveRequest leaveRequest = toEntity(request);
        leaveRequest.setDays(days);
        leaveRequest.setStatus(LeaveRequestStatus.PENDING);

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        return toResponseDTO(saved);
    }

    public List<LeaveRequestResponseDTO> getAllLeaveRequests(Long employeeId, LeaveRequestStatus status) {
        List<LeaveRequest> list;
        if (employeeId != null && status != null) {
            list = leaveRequestRepository.findByEmployeeIdAndStatus(employeeId, status);
        } else if (employeeId != null) {
            list = leaveRequestRepository.findByEmployeeId(employeeId);
        } else if (status != null) {
            list = leaveRequestRepository.findByStatus(status);
        } else {
            list = leaveRequestRepository.findAll();
        }

        List<LeaveRequestResponseDTO> responses = new ArrayList<>();
        for (LeaveRequest lr : list) {
            responses.add(toResponseDTO(lr));
        }
        return responses;
    }

    public LeaveRequestResponseDTO getLeaveRequestById(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave request not found with id: " + id));
        return toResponseDTO(leaveRequest);
    }

    public LeaveRequestResponseDTO updateLeaveRequest(Long id, LeaveRequestRequestDTO request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave request not found with id: " + id));

        if (leaveRequest.getStatus() != LeaveRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leave request can only be updated while in PENDING status");
        }

        if (request.getFromDate().isAfter(request.getToDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromDate must be on or before toDate");
        }

        if (request.getFromDate().getYear() != request.getToDate().getYear()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leave request cannot span across multiple calendar years");
        }

        int days = (int) ChronoUnit.DAYS.between(request.getFromDate(), request.getToDate()) + 1;

        leaveRequest.setEmployeeId(request.getEmployeeId());
        leaveRequest.setLeaveTypeId(request.getLeaveTypeId());
        leaveRequest.setFromDate(request.getFromDate());
        leaveRequest.setToDate(request.getToDate());
        leaveRequest.setDays(days);
        leaveRequest.setReason(request.getReason());

        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        return toResponseDTO(updated);
    }

    public void deleteLeaveRequest(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave request not found with id: " + id));

        if (leaveRequest.getStatus() != LeaveRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leave request can only be deleted while in PENDING status");
        }

        leaveRequestRepository.delete(leaveRequest);
    }

    @Transactional
    public LeaveRequestResponseDTO approveLeaveRequest(Long id, Long decidedById) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave request not found with id: " + id));

        if (leaveRequest.getStatus() != LeaveRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PENDING leave requests can be approved");
        }

        if (decidedById.equals(leaveRequest.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Decider cannot be the employee requesting the leave");
        }

        Employee decider = employeeRepository.findById(decidedById)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Decider not found with id: " + decidedById));

        if (decider.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Decider must be an active or working employee");
        }

        LeaveType leaveType = leaveTypeRepository.findById(leaveRequest.getLeaveTypeId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave type not found with id: " + leaveRequest.getLeaveTypeId()));

        if (Boolean.TRUE.equals(leaveType.getPaid())) {
            LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndBalanceYear(
                    leaveRequest.getEmployeeId(), leaveRequest.getLeaveTypeId(), leaveRequest.getFromDate().getYear())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No leave balance found to deduct approved leave"));

            balance.setUsedDays(balance.getUsedDays() + leaveRequest.getDays());
            leaveBalanceRepository.save(balance);
        }

        for (LocalDate date = leaveRequest.getFromDate(); !date.isAfter(leaveRequest.getToDate()); date = date.plusDays(1)) {
            if (!attendanceRepository.existsByEmployeeIdAndAttendanceDate(leaveRequest.getEmployeeId(), date)) {
                Attendance attendance = new Attendance();
                attendance.setEmployeeId(leaveRequest.getEmployeeId());
                attendance.setAttendanceDate(date);
                attendance.setStatus(AttendanceStatus.ON_LEAVE);
                attendanceRepository.save(attendance);
            }
        }

        leaveRequest.setStatus(LeaveRequestStatus.APPROVED);
        leaveRequest.setDecidedById(decidedById);
        leaveRequest.setDecidedOn(LocalDate.now());

        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        return toResponseDTO(updated);
    }

    public LeaveRequestResponseDTO rejectLeaveRequest(Long id, Long decidedById) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave request not found with id: " + id));

        if (leaveRequest.getStatus() != LeaveRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PENDING leave requests can be rejected");
        }

        if (decidedById.equals(leaveRequest.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Decider cannot be the employee requesting the leave");
        }

        Employee decider = employeeRepository.findById(decidedById)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Decider not found with id: " + decidedById));

        if (decider.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Decider must be an active or working employee");
        }

        leaveRequest.setStatus(LeaveRequestStatus.REJECTED);
        leaveRequest.setDecidedById(decidedById);
        leaveRequest.setDecidedOn(LocalDate.now());

        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        return toResponseDTO(updated);
    }

    public LeaveRequestResponseDTO cancelLeaveRequest(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave request not found with id: " + id));

        if (leaveRequest.getStatus() != LeaveRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leave request can only be cancelled while in PENDING status");
        }

        leaveRequest.setStatus(LeaveRequestStatus.CANCELLED);
        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        return toResponseDTO(updated);
    }

    private LeaveRequest toEntity(LeaveRequestRequestDTO dto) {
        LeaveRequest lr = new LeaveRequest();
        lr.setEmployeeId(dto.getEmployeeId());
        lr.setLeaveTypeId(dto.getLeaveTypeId());
        lr.setFromDate(dto.getFromDate());
        lr.setToDate(dto.getToDate());
        lr.setReason(dto.getReason());
        return lr;
    }

    private LeaveRequestResponseDTO toResponseDTO(LeaveRequest entity) {
        return new LeaveRequestResponseDTO(
            entity.getId(),
            entity.getEmployeeId(),
            entity.getLeaveTypeId(),
            entity.getFromDate(),
            entity.getToDate(),
            entity.getDays(),
            entity.getReason(),
            entity.getStatus(),
            entity.getDecidedById(),
            entity.getDecidedOn()
        );
    }
}
