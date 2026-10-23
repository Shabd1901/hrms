package com.peopleops.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.peopleops.dto.JobOpeningRequestDTO;
import com.peopleops.dto.JobOpeningResponseDTO;
import com.peopleops.enums.JobOpeningStatus;
import com.peopleops.service.JobOpeningService;
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

@Tag(name = "Job Openings", description = "Job opening management APIs")
@RestController
@RequestMapping("/api/job-openings")
public class JobOpeningController {

    private final JobOpeningService jobOpeningService;

    public JobOpeningController(JobOpeningService jobOpeningService) {
        this.jobOpeningService = jobOpeningService;
    }

    @PostMapping
    public ResponseEntity<JobOpeningResponseDTO> createJobOpening(@Valid @RequestBody JobOpeningRequestDTO request) {
        JobOpeningResponseDTO response = jobOpeningService.createJobOpening(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobOpeningResponseDTO>> getAllJobOpenings(
            @RequestParam(required = false) JobOpeningStatus status) {
        List<JobOpeningResponseDTO> openings = jobOpeningService.getAllJobOpenings(status);
        return ResponseEntity.ok(openings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOpeningResponseDTO> getJobOpeningById(@PathVariable Long id) {
        JobOpeningResponseDTO response = jobOpeningService.getJobOpeningById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobOpeningResponseDTO> updateJobOpening(
            @PathVariable Long id,
            @Valid @RequestBody JobOpeningRequestDTO request) {
        JobOpeningResponseDTO response = jobOpeningService.updateJobOpening(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobOpening(@PathVariable Long id) {
        jobOpeningService.deleteJobOpening(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobOpeningResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam JobOpeningStatus value) {
        JobOpeningResponseDTO response = jobOpeningService.updateStatus(id, value);
        return ResponseEntity.ok(response);
    }
}
