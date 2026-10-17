package com.peopleops.service;

import com.peopleops.dto.LeaveBalanceRequestDTO;
import com.peopleops.dto.LeaveBalanceResponseDTO;
import com.peopleops.entity.LeaveBalance;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.LeaveBalanceRepository;
import com.peopleops.repository.LeaveTypeRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LeaveBalanceService {

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
        if (!employeeRepository.existsById(request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId());
        }
        if (!leaveTypeRepository.existsById(request.getLeaveTypeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave type not found with id: " + request.getLeaveTypeId());
        }

        if (leaveBalanceRepository.existsByEmployeeIdAndLeaveTypeIdAndBalanceYear(
                request.getEmployeeId(), request.getLeaveTypeId(), request.getBalanceYear())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Leave balance already exists for this employee, leave type, and year");
        }

        LeaveBalance balance = toEntity(request);
        if (balance.getUsedDays() == null) {
            balance.setUsedDays(0);
        }
        LeaveBalance saved = leaveBalanceRepository.save(balance);
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave balance not found with id: " + id));
        return toResponseDTO(balance);
    }

    public LeaveBalanceResponseDTO updateLeaveBalance(Long id, LeaveBalanceRequestDTO request) {
        LeaveBalance balance = leaveBalanceRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave balance not found with id: " + id));

        if (!employeeRepository.existsById(request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId());
        }
        if (!leaveTypeRepository.existsById(request.getLeaveTypeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave type not found with id: " + request.getLeaveTypeId());
        }

        balance.setEmployeeId(request.getEmployeeId());
        balance.setLeaveTypeId(request.getLeaveTypeId());
        balance.setBalanceYear(request.getBalanceYear());
        balance.setTotalDays(request.getTotalDays());
        balance.setUsedDays(request.getUsedDays());

        LeaveBalance updated = leaveBalanceRepository.save(balance);
        return toResponseDTO(updated);
    }

    public void deleteLeaveBalance(Long id) {
        LeaveBalance balance = leaveBalanceRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave balance not found with id: " + id));
        leaveBalanceRepository.delete(balance);
    }

    private LeaveBalance toEntity(LeaveBalanceRequestDTO dto) {
        LeaveBalance lb = new LeaveBalance();
        lb.setEmployeeId(dto.getEmployeeId());
        lb.setLeaveTypeId(dto.getLeaveTypeId());
        lb.setBalanceYear(dto.getBalanceYear());
        lb.setTotalDays(dto.getTotalDays());
        lb.setUsedDays(dto.getUsedDays());
        return lb;
    }

    private LeaveBalanceResponseDTO toResponseDTO(LeaveBalance entity) {
        return new LeaveBalanceResponseDTO(
            entity.getId(),
            entity.getEmployeeId(),
            entity.getLeaveTypeId(),
            entity.getBalanceYear(),
            entity.getTotalDays(),
            entity.getUsedDays()
        );
    }
}
