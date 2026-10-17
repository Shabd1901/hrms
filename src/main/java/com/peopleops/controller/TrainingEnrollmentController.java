package com.peopleops.controller;

import com.peopleops.dto.TrainingEnrollmentRequestDTO;
import com.peopleops.dto.TrainingEnrollmentResponseDTO;
import com.peopleops.service.TrainingEnrollmentService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/training-enrollments")
public class TrainingEnrollmentController {

    private final TrainingEnrollmentService trainingEnrollmentService;

    public TrainingEnrollmentController(TrainingEnrollmentService trainingEnrollmentService) {
        this.trainingEnrollmentService = trainingEnrollmentService;
    }

    @PostMapping
    public ResponseEntity<TrainingEnrollmentResponseDTO> enrollEmployee(
            @RequestBody TrainingEnrollmentRequestDTO request) {
        TrainingEnrollmentResponseDTO response = trainingEnrollmentService.enrollEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TrainingEnrollmentResponseDTO>> getAllEnrollments(
            @RequestParam(required = false) Long employeeId) {
        List<TrainingEnrollmentResponseDTO> list = trainingEnrollmentService.getAllEnrollments(employeeId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingEnrollmentResponseDTO> getEnrollmentById(@PathVariable Long id) {
        TrainingEnrollmentResponseDTO response = trainingEnrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        trainingEnrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TrainingEnrollmentResponseDTO> completeEnrollment(@PathVariable Long id) {
        TrainingEnrollmentResponseDTO response = trainingEnrollmentService.completeEnrollment(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/drop")
    public ResponseEntity<TrainingEnrollmentResponseDTO> dropEnrollment(@PathVariable Long id) {
        TrainingEnrollmentResponseDTO response = trainingEnrollmentService.dropEnrollment(id);
        return ResponseEntity.ok(response);
    }
}
