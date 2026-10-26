package com.peopleops.service;

import com.peopleops.dto.LeaveBalanceRequestDTO;
import com.peopleops.dto.LeaveBalanceResponseDTO;
import com.peopleops.entity.Employee;
import com.peopleops.entity.LeaveBalance;
import com.peopleops.entity.LeaveType;
import com.peopleops.exception.DuplicateResourceException;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.LeaveBalanceRepository;
import com.peopleops.repository.LeaveTypeRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LeaveBalanceService {

    private static final Logger log = LoggerFactory.getLogger(LeaveBalanceService.class);

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveBalanceService(
            LeaveBalanceRepository leaveBalanceRepository,
            EmployeeRepository employeeRepository,
            LeaveTypeRepository leaveTypeRepository) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
    }

    public LeaveBalanceResponseDTO createLeaveBalance(LeaveBalanceRequestDTO request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> {
                log.warn("Leave balance creation failed: employee id {} not found", request.getEmployeeId());
                return new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId());
            });
        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
            .orElseThrow(() -> {
                log.warn("Leave balance creation failed: leave type id {} not found", request.getLeaveTypeId());
                return new ResourceNotFoundException("Leave type not found with id: " + request.getLeaveTypeId());
            });

        if (leaveBalanceRepository.existsByEmployeeIdAndLeaveTypeIdAndBalanceYear(
                request.getEmployeeId(), request.getLeaveTypeId(), request.getBalanceYear())) {
            log.warn("Leave balance creation failed: duplicate balance for employee id {}, leave type id {}, year {}", request.getEmployeeId(), request.getLeaveTypeId(), request.getBalanceYear());
            throw new DuplicateResourceException("Leave balance already exists for this employee, leave type, and year");
        }

        LeaveBalance balance = toEntity(request);
        balance.setEmployee(employee);
        balance.setLeaveType(leaveType);
        if (balance.getUsedDays() == null) {
            balance.setUsedDays(0);
        }
        LeaveBalance saved = leaveBalanceRepository.save(balance);
        log.info("Created leave balance id: {} for employee id: {} and leave type id: {} for year: {}", saved.getId(), saved.getEmployee() != null ? saved.getEmployee().getId() : null, saved.getLeaveType() != null ? saved.getLeaveType().getId() : null, saved.getBalanceYear());
        return toResponseDTO(saved);
    }

    public List<LeaveBalanceResponseDTO> getAllLeaveBalances(Long employeeId, Integer year) {
        List<LeaveBalance> list;
        if (employeeId != null && year != null) {
            list = leaveBalanceRepository.findByEmployeeIdAndBalanceYear(employeeId, year);
        } else if (employeeId != null) {
            list = leaveBalanceRepository.findByEmployeeId(employeeId);
        } else if (year != null) {
            list = leaveBalanceRepository.findByBalanceYear(year);
        } else {
            list = leaveBalanceRepository.findAll();
        }

        List<LeaveBalanceResponseDTO> responses = new ArrayList<>();
        for (LeaveBalance lb : list) {
            responses.add(toResponseDTO(lb));
        }
        return responses;
    }

    public LeaveBalanceResponseDTO getLeaveBalanceById(Long id) {
        LeaveBalance balance = leaveBalanceRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Leave balance lookup failed: id {} not found", id);
                return new ResourceNotFoundException("Leave balance not found with id: " + id);
            });
        return toResponseDTO(balance);
    }

    public LeaveBalanceResponseDTO updateLeaveBalance(Long id, LeaveBalanceRequestDTO request) {
        LeaveBalance balance = leaveBalanceRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Leave balance update failed: id {} not found", id);
                return new ResourceNotFoundException("Leave balance not found with id: " + id);
            });

        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> {
                log.warn("Leave balance update failed: employee id {} not found", request.getEmployeeId());
                return new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId());
            });
        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
            .orElseThrow(() -> {
                log.warn("Leave balance update failed: leave type id {} not found", request.getLeaveTypeId());
                return new ResourceNotFoundException("Leave type not found with id: " + request.getLeaveTypeId());
            });

        balance.setEmployee(employee);
        balance.setLeaveType(leaveType);
        balance.setBalanceYear(request.getBalanceYear());
        balance.setTotalDays(request.getTotalDays());
        balance.setUsedDays(request.getUsedDays());

        LeaveBalance updated = leaveBalanceRepository.save(balance);
        log.info("Updated leave balance id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteLeaveBalance(Long id) {
        LeaveBalance balance = leaveBalanceRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Leave balance delete failed: id {} not found", id);
                return new ResourceNotFoundException("Leave balance not found with id: " + id);
            });
        leaveBalanceRepository.delete(balance);
        log.info("Deleted leave balance id: {}", id);
    }

    private LeaveBalance toEntity(LeaveBalanceRequestDTO dto) {
        LeaveBalance lb = new LeaveBalance();
        lb.setBalanceYear(dto.getBalanceYear());
        lb.setTotalDays(dto.getTotalDays());
        lb.setUsedDays(dto.getUsedDays());
        return lb;
    }

    private LeaveBalanceResponseDTO toResponseDTO(LeaveBalance entity) {
        return new LeaveBalanceResponseDTO(
            entity.getId(),
            entity.getEmployee() != null ? entity.getEmployee().getId() : null,
            entity.getLeaveType() != null ? entity.getLeaveType().getId() : null,
            entity.getBalanceYear(),
            entity.getTotalDays(),
            entity.getUsedDays()
        );
    }
}
