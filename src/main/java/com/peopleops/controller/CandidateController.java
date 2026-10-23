package com.peopleops.controller;

import com.peopleops.dto.CandidateRequestDTO;
import com.peopleops.dto.CandidateResponseDTO;
import com.peopleops.dto.EmployeeResponseDTO;
import com.peopleops.dto.HireRequestDTO;
import com.peopleops.enums.CandidateStage;
import com.peopleops.service.CandidateService;
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

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public ResponseEntity<CandidateResponseDTO> createCandidate(@Valid @RequestBody CandidateRequestDTO request) {
        CandidateResponseDTO response = candidateService.createCandidate(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CandidateResponseDTO>> getAllCandidates(
            @RequestParam(required = false) Long jobOpeningId) {
        List<CandidateResponseDTO> candidates = candidateService.getAllCandidates(jobOpeningId);
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> getCandidateById(@PathVariable Long id) {
        CandidateResponseDTO response = candidateService.getCandidateById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> updateCandidate(
            @PathVariable Long id,
            @Valid @RequestBody CandidateRequestDTO request) {
        CandidateResponseDTO response = candidateService.updateCandidate(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stage")
    public ResponseEntity<CandidateResponseDTO> updateStage(
            @PathVariable Long id,
            @RequestParam CandidateStage value) {
        CandidateResponseDTO response = candidateService.updateStage(id, value);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/hire")
    public ResponseEntity<EmployeeResponseDTO> hireCandidate(
            @PathVariable Long id,
            @Valid @RequestBody HireRequestDTO request) {
        EmployeeResponseDTO response = candidateService.hireCandidate(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
