package com.peopleops.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.peopleops.dto.PerformanceReviewRequestDTO;
import com.peopleops.dto.PerformanceReviewResponseDTO;
import com.peopleops.service.PerformanceReviewService;
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

@Tag(name = "Performance Reviews", description = "Employee performance appraisal and review APIs")
@RestController
@RequestMapping("/api/performance-reviews")
public class PerformanceReviewController {

    private final PerformanceReviewService performanceReviewService;

    public PerformanceReviewController(PerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    @PostMapping
    public ResponseEntity<PerformanceReviewResponseDTO> createPerformanceReview(
            @Valid @RequestBody PerformanceReviewRequestDTO request) {
        PerformanceReviewResponseDTO response = performanceReviewService.createPerformanceReview(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PerformanceReviewResponseDTO>> getAllPerformanceReviews(
            @RequestParam(required = false) Long employeeId) {
        List<PerformanceReviewResponseDTO> list = performanceReviewService.getAllPerformanceReviews(employeeId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceReviewResponseDTO> getPerformanceReviewById(@PathVariable Long id) {
        PerformanceReviewResponseDTO response = performanceReviewService.getPerformanceReviewById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerformanceReviewResponseDTO> updatePerformanceReview(
            @PathVariable Long id,
            @Valid @RequestBody PerformanceReviewRequestDTO request) {
        PerformanceReviewResponseDTO response = performanceReviewService.updatePerformanceReview(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformanceReview(@PathVariable Long id) {
        performanceReviewService.deletePerformanceReview(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/submit")
    public ResponseEntity<PerformanceReviewResponseDTO> submitPerformanceReview(@PathVariable Long id) {
        PerformanceReviewResponseDTO response = performanceReviewService.submitPerformanceReview(id);
        return ResponseEntity.ok(response);
    }
}
