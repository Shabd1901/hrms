package com.peopleops.controller;

import com.peopleops.dto.AttendanceRequestDTO;
import com.peopleops.dto.AttendanceResponseDTO;
import com.peopleops.service.AttendanceService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
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

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<AttendanceResponseDTO> createAttendance(@Valid @RequestBody AttendanceRequestDTO request) {
        AttendanceResponseDTO response = attendanceService.createAttendance(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponseDTO>> getAllAttendance(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) LocalDate date) {
        List<AttendanceResponseDTO> response = attendanceService.getAllAttendance(employeeId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponseDTO> getAttendanceById(@PathVariable Long id) {
        AttendanceResponseDTO response = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponseDTO> updateAttendance(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceRequestDTO request) {
        AttendanceResponseDTO response = attendanceService.updateAttendance(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/check-out")
    public ResponseEntity<AttendanceResponseDTO> checkOut(
            @PathVariable Long id,
            @RequestParam LocalTime time) {
        AttendanceResponseDTO response = attendanceService.checkOut(id, time);
        return ResponseEntity.ok(response);
    }
}
