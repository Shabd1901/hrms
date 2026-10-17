package com.peopleops.controller;

import com.peopleops.dto.LeaveBalanceRequestDTO;
import com.peopleops.dto.LeaveBalanceResponseDTO;
import com.peopleops.service.LeaveBalanceService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leave-balances")
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    public LeaveBalanceController(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @PostMapping
    public ResponseEntity<LeaveBalanceResponseDTO> createLeaveBalance(@RequestBody LeaveBalanceRequestDTO request) {
        LeaveBalanceResponseDTO response = leaveBalanceService.createLeaveBalance(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LeaveBalanceResponseDTO>> getAllLeaveBalances(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Integer year) {
        List<LeaveBalanceResponseDTO> list = leaveBalanceService.getAllLeaveBalances(employeeId, year);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveBalanceResponseDTO> getLeaveBalanceById(@PathVariable Long id) {
        LeaveBalanceResponseDTO response = leaveBalanceService.getLeaveBalanceById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveBalanceResponseDTO> updateLeaveBalance(
            @PathVariable Long id,
            @RequestBody LeaveBalanceRequestDTO request) {
        LeaveBalanceResponseDTO response = leaveBalanceService.updateLeaveBalance(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveBalance(@PathVariable Long id) {
        leaveBalanceService.deleteLeaveBalance(id);
        return ResponseEntity.noContent().build();
    }
}
