package com.peopleops.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.peopleops.dto.InterviewRequestDTO;
import com.peopleops.dto.InterviewResponseDTO;
import com.peopleops.enums.InterviewResult;
import com.peopleops.service.InterviewService;
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

@Tag(name = "Interviews", description = "Candidate interview scheduling and evaluation APIs")
@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public ResponseEntity<InterviewResponseDTO> createInterview(@Valid @RequestBody InterviewRequestDTO request) {
        InterviewResponseDTO response = interviewService.createInterview(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InterviewResponseDTO>> getAllInterviews(
            @RequestParam(required = false) Long candidateId) {
        List<InterviewResponseDTO> interviews = interviewService.getAllInterviews(candidateId);
        return ResponseEntity.ok(interviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponseDTO> getInterviewById(@PathVariable Long id) {
        InterviewResponseDTO response = interviewService.getInterviewById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponseDTO> updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody InterviewRequestDTO request) {
        InterviewResponseDTO response = interviewService.updateInterview(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }

        @Operation(summary = "Record interview result and feedback")
@PatchMapping("/{id}/result")
    public ResponseEntity<InterviewResponseDTO> updateResult(
            @PathVariable Long id,
            @RequestParam InterviewResult value,
            @RequestParam(required = false) String feedback) {
        InterviewResponseDTO response = interviewService.updateResult(id, value, feedback);
        return ResponseEntity.ok(response);
    }
}
