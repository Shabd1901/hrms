package com.peopleops.controller;

import com.peopleops.dto.DesignationRequestDTO;
import com.peopleops.dto.DesignationResponseDTO;
import com.peopleops.service.DesignationService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/designations")
public class DesignationController {

    private final DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @PostMapping
    public ResponseEntity<DesignationResponseDTO> createDesignation(@Valid @RequestBody DesignationRequestDTO request) {
        DesignationResponseDTO response = designationService.createDesignation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DesignationResponseDTO>> getAllDesignations() {
        List<DesignationResponseDTO> designations = designationService.getAllDesignations();
        return ResponseEntity.ok(designations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesignationResponseDTO> getDesignationById(@PathVariable Long id) {
        DesignationResponseDTO response = designationService.getDesignationById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DesignationResponseDTO> updateDesignation(
            @PathVariable Long id,
            @Valid @RequestBody DesignationRequestDTO request) {
        DesignationResponseDTO response = designationService.updateDesignation(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDesignation(@PathVariable Long id) {
        designationService.deleteDesignation(id);
        return ResponseEntity.noContent().build();
    }
}
