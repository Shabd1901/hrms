package com.peopleops.controller;

import com.peopleops.dto.LeaveTypeRequestDTO;
import com.peopleops.dto.LeaveTypeResponseDTO;
import com.peopleops.service.LeaveTypeService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leave-types")
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    public LeaveTypeController(LeaveTypeService leaveTypeService) {
        this.leaveTypeService = leaveTypeService;
    }

    @PostMapping
    public ResponseEntity<LeaveTypeResponseDTO> createLeaveType(@RequestBody LeaveTypeRequestDTO request) {
        LeaveTypeResponseDTO response = leaveTypeService.createLeaveType(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LeaveTypeResponseDTO>> getAllLeaveTypes() {
        List<LeaveTypeResponseDTO> list = leaveTypeService.getAllLeaveTypes();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveTypeResponseDTO> getLeaveTypeById(@PathVariable Long id) {
        LeaveTypeResponseDTO response = leaveTypeService.getLeaveTypeById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveTypeResponseDTO> updateLeaveType(
            @PathVariable Long id,
            @RequestBody LeaveTypeRequestDTO request) {
        LeaveTypeResponseDTO response = leaveTypeService.updateLeaveType(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveType(@PathVariable Long id) {
        leaveTypeService.deleteLeaveType(id);
        return ResponseEntity.noContent().build();
    }
}
