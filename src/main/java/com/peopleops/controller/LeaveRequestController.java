package com.peopleops.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.peopleops.dto.LeaveRequestRequestDTO;
import com.peopleops.dto.LeaveRequestResponseDTO;
import com.peopleops.enums.LeaveRequestStatus;
import com.peopleops.service.LeaveRequestService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Leave Requests", description = "Leave application and approval workflow APIs")
@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping
    public ResponseEntity<LeaveRequestResponseDTO> createLeaveRequest(@Valid @RequestBody LeaveRequestRequestDTO request) {
        LeaveRequestResponseDTO response = leaveRequestService.createLeaveRequest(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequestResponseDTO>> getAllLeaveRequests(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) LeaveRequestStatus status) {
        List<LeaveRequestResponseDTO> list = leaveRequestService.getAllLeaveRequests(employeeId, status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestResponseDTO> getLeaveRequestById(@PathVariable Long id) {
        LeaveRequestResponseDTO response = leaveRequestService.getLeaveRequestById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequestResponseDTO> updateLeaveRequest(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestRequestDTO request) {
        LeaveRequestResponseDTO response = leaveRequestService.updateLeaveRequest(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.noContent().build();
    }

        @Operation(summary = "Approve leave request, deduct balance, and mark attendance (BR-05, BR-06)")
@PatchMapping("/{id}/approve")
    public ResponseEntity<LeaveRequestResponseDTO> approveLeaveRequest(
            @PathVariable Long id,
            @RequestParam Long decidedById) {
        LeaveRequestResponseDTO response = leaveRequestService.approveLeaveRequest(id, decidedById);
        return ResponseEntity.ok(response);
    }

        @Operation(summary = "Reject submitted leave request (BR-05)")
@PatchMapping("/{id}/reject")
    public ResponseEntity<LeaveRequestResponseDTO> rejectLeaveRequest(
            @PathVariable Long id,
            @RequestParam Long decidedById) {
        LeaveRequestResponseDTO response = leaveRequestService.rejectLeaveRequest(id, decidedById);
        return ResponseEntity.ok(response);
    }

        @Operation(summary = "Cancel approved leave request and restore balance (BR-05)")
@PatchMapping("/{id}/cancel")
    public ResponseEntity<LeaveRequestResponseDTO> cancelLeaveRequest(@PathVariable Long id) {
        LeaveRequestResponseDTO response = leaveRequestService.cancelLeaveRequest(id);
        return ResponseEntity.ok(response);
    }
}
