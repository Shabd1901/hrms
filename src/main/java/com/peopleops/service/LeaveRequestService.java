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
import com.peopleops.exception.BusinessRuleException;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.AttendanceRepository;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.LeaveBalanceRepository;
import com.peopleops.repository.LeaveRequestRepository;
import com.peopleops.repository.LeaveTypeRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeaveRequestService {

    private static final Logger log = LoggerFactory.getLogger(LeaveRequestService.class);

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
            .orElseThrow(() -> {
                log.warn("Leave request creation failed: employee id {} not found", request.getEmployeeId());
                return new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId());
            });

        if (employee.getStatus() == EmployeeStatus.EXITED) {
            log.warn("Leave request creation failed: employee id {} is EXITED", employee.getId());
            throw new BusinessRuleException("Only working employees can request leave");
        }

        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
            .orElseThrow(() -> {
                log.warn("Leave request creation failed: leave type id {} not found", request.getLeaveTypeId());
                return new ResourceNotFoundException("Leave type not found with id: " + request.getLeaveTypeId());
            });

        if (request.getFromDate().isAfter(request.getToDate())) {
            log.warn("Leave request creation failed: fromDate {} is after toDate {}", request.getFromDate(), request.getToDate());
            throw new BusinessRuleException("fromDate must be on or before toDate");
        }

        if (request.getFromDate().getYear() != request.getToDate().getYear()) {
            log.warn("Leave request creation failed: crosses calendar year boundary ({} to {})", request.getFromDate(), request.getToDate());
            throw new BusinessRuleException("Leave request cannot span across multiple calendar years");
        }

        int days = (int) ChronoUnit.DAYS.between(request.getFromDate(), request.getToDate()) + 1;

        List<LeaveRequest> existing = leaveRequestRepository.findByEmployeeId(request.getEmployeeId());
        for (LeaveRequest lr : existing) {
            if (lr.getStatus() == LeaveRequestStatus.PENDING || lr.getStatus() == LeaveRequestStatus.APPROVED) {
                boolean overlap = !(lr.getToDate().isBefore(request.getFromDate()) || lr.getFromDate().isAfter(request.getToDate()));
                if (overlap) {
                    log.warn("Leave request creation failed for employee id {}: overlaps with existing request id {}", request.getEmployeeId(), lr.getId());
                    throw new BusinessRuleException("Leave request dates overlap with an existing pending or approved request");
                }
            }
        }

        if (Boolean.TRUE.equals(leaveType.getPaid())) {
            LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndBalanceYear(request.getEmployeeId(), request.getLeaveTypeId(), request.getFromDate().getYear())
                .orElseThrow(() -> {
                    log.warn("Leave request creation failed: no leave balance found for employee id {}, leave type id {}, year {}", request.getEmployeeId(), request.getLeaveTypeId(), request.getFromDate().getYear());
                    return new BusinessRuleException("No leave balance found for this leave type and year");
                });

            int available = balance.getTotalDays() - balance.getUsedDays();
            if (available < days) {
                log.warn("Leave request creation failed: insufficient balance (available: {}, requested: {}) for employee id {}", available, days, request.getEmployeeId());
                throw new BusinessRuleException("Insufficient leave balance: available " + available + ", requested " + days);
            }
        }

        LeaveRequest leaveRequest = toEntity(request);
        leaveRequest.setDays(days);
        leaveRequest.setStatus(LeaveRequestStatus.PENDING);

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        log.info("Created leave request id: {} for employee id: {} for {} days from {} to {}", saved.getId(), saved.getEmployeeId(), saved.getDays(), saved.getFromDate(), saved.getToDate());
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
            .orElseThrow(() -> {
                log.warn("Leave request lookup failed: id {} not found", id);
                return new ResourceNotFoundException("Leave request not found with id: " + id);
            });
        return toResponseDTO(leaveRequest);
    }

    public LeaveRequestResponseDTO updateLeaveRequest(Long id, LeaveRequestRequestDTO request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Leave request update failed: id {} not found", id);
                return new ResourceNotFoundException("Leave request not found with id: " + id);
            });

        if (leaveRequest.getStatus() != LeaveRequestStatus.PENDING) {
            log.warn("Leave request update failed for id {}: status is {}", id, leaveRequest.getStatus());
            throw new BusinessRuleException("Leave request can only be updated while in PENDING status");
        }

        if (request.getFromDate().isAfter(request.getToDate())) {
            log.warn("Leave request update failed for id {}: fromDate is after toDate", id);
            throw new BusinessRuleException("fromDate must be on or before toDate");
        }

        if (request.getFromDate().getYear() != request.getToDate().getYear()) {
            log.warn("Leave request update failed for id {}: cross year boundary", id);
            throw new BusinessRuleException("Leave request cannot span across multiple calendar years");
        }

        int days = (int) ChronoUnit.DAYS.between(request.getFromDate(), request.getToDate()) + 1;

        leaveRequest.setEmployeeId(request.getEmployeeId());
        leaveRequest.setLeaveTypeId(request.getLeaveTypeId());
        leaveRequest.setFromDate(request.getFromDate());
        leaveRequest.setToDate(request.getToDate());
        leaveRequest.setDays(days);
        leaveRequest.setReason(request.getReason());

        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        log.info("Updated leave request id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteLeaveRequest(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Leave request delete failed: id {} not found", id);
                return new ResourceNotFoundException("Leave request not found with id: " + id);
            });

        if (leaveRequest.getStatus() != LeaveRequestStatus.PENDING) {
            log.warn("Leave request delete failed for id {}: status is {}", id, leaveRequest.getStatus());
            throw new BusinessRuleException("Leave request can only be deleted while in PENDING status");
        }

        leaveRequestRepository.delete(leaveRequest);
        log.info("Deleted leave request id: {}", id);
    }

    @Transactional
    public LeaveRequestResponseDTO approveLeaveRequest(Long id, Long decidedById) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Leave request approval failed: id {} not found", id);
                return new ResourceNotFoundException("Leave request not found with id: " + id);
            });

        if (leaveRequest.getStatus() != LeaveRequestStatus.PENDING) {
            log.warn("Leave request approval failed for id {}: status is {}", id, leaveRequest.getStatus());
            throw new BusinessRuleException("Only PENDING leave requests can be approved");
        }

        if (decidedById.equals(leaveRequest.getEmployeeId())) {
            log.warn("Leave request approval failed for id {}: decider cannot be requester", id);
            throw new BusinessRuleException("Decider cannot be the employee requesting the leave");
        }

        Employee decider = employeeRepository.findById(decidedById)
            .orElseThrow(() -> {
                log.warn("Leave request approval failed for id {}: decider id {} not found", id, decidedById);
                return new ResourceNotFoundException("Decider not found with id: " + decidedById);
            });

        if (decider.getStatus() == EmployeeStatus.EXITED) {
            log.warn("Leave request approval failed for id {}: decider id {} is EXITED", id, decidedById);
            throw new BusinessRuleException("Decider must be an active or working employee");
        }

        LeaveType leaveType = leaveTypeRepository.findById(leaveRequest.getLeaveTypeId())
            .orElseThrow(() -> {
                log.warn("Leave request approval failed for id {}: leave type id {} not found", id, leaveRequest.getLeaveTypeId());
                return new ResourceNotFoundException("Leave type not found with id: " + leaveRequest.getLeaveTypeId());
            });

        if (Boolean.TRUE.equals(leaveType.getPaid())) {
            LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndBalanceYear(
                    leaveRequest.getEmployeeId(), leaveRequest.getLeaveTypeId(), leaveRequest.getFromDate().getYear())
                .orElseThrow(() -> {
                    log.warn("Leave request approval failed for id {}: no balance found", id);
                    return new BusinessRuleException("No leave balance found to deduct approved leave");
                });

            balance.setUsedDays(balance.getUsedDays() + leaveRequest.getDays());
            leaveBalanceRepository.save(balance);
        }

        Employee employee = employeeRepository.findById(leaveRequest.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + leaveRequest.getEmployeeId()));

        for (LocalDate date = leaveRequest.getFromDate(); !date.isAfter(leaveRequest.getToDate()); date = date.plusDays(1)) {
            if (!attendanceRepository.existsByEmployeeIdAndAttendanceDate(leaveRequest.getEmployeeId(), date)) {
                Attendance attendance = new Attendance();
                attendance.setEmployee(employee);
                attendance.setAttendanceDate(date);
                attendance.setStatus(AttendanceStatus.ON_LEAVE);
                attendanceRepository.save(attendance);
            }
        }

        leaveRequest.setStatus(LeaveRequestStatus.APPROVED);
        leaveRequest.setDecidedById(decidedById);
        leaveRequest.setDecidedOn(LocalDate.now());

        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        log.info("Approved leave request id: {} by decider id: {} and synchronized ON_LEAVE attendance records", id, decidedById);
        return toResponseDTO(updated);
    }

    public LeaveRequestResponseDTO rejectLeaveRequest(Long id, Long decidedById) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Leave request rejection failed: id {} not found", id);
                return new ResourceNotFoundException("Leave request not found with id: " + id);
            });

        if (leaveRequest.getStatus() != LeaveRequestStatus.PENDING) {
            log.warn("Leave request rejection failed for id {}: status is {}", id, leaveRequest.getStatus());
            throw new BusinessRuleException("Only PENDING leave requests can be rejected");
        }

        if (decidedById.equals(leaveRequest.getEmployeeId())) {
            log.warn("Leave request rejection failed for id {}: decider cannot be requester", id);
            throw new BusinessRuleException("Decider cannot be the employee requesting the leave");
        }

        Employee decider = employeeRepository.findById(decidedById)
            .orElseThrow(() -> {
                log.warn("Leave request rejection failed for id {}: decider id {} not found", id, decidedById);
                return new ResourceNotFoundException("Decider not found with id: " + decidedById);
            });

        if (decider.getStatus() == EmployeeStatus.EXITED) {
            log.warn("Leave request rejection failed for id {}: decider id {} is EXITED", id, decidedById);
            throw new BusinessRuleException("Decider must be an active or working employee");
        }

        leaveRequest.setStatus(LeaveRequestStatus.REJECTED);
        leaveRequest.setDecidedById(decidedById);
        leaveRequest.setDecidedOn(LocalDate.now());

        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        log.info("Rejected leave request id: {} by decider id: {}", id, decidedById);
        return toResponseDTO(updated);
    }

    public LeaveRequestResponseDTO cancelLeaveRequest(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Leave request cancel failed: id {} not found", id);
                return new ResourceNotFoundException("Leave request not found with id: " + id);
            });

        if (leaveRequest.getStatus() != LeaveRequestStatus.PENDING) {
            log.warn("Leave request cancel failed for id {}: status is {}", id, leaveRequest.getStatus());
            throw new BusinessRuleException("Leave request can only be cancelled while in PENDING status");
        }

        leaveRequest.setStatus(LeaveRequestStatus.CANCELLED);
        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        log.info("Cancelled leave request id: {}", id);
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
