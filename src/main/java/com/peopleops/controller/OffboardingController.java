package com.peopleops.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.peopleops.dto.OffboardingRequestDTO;
import com.peopleops.dto.OffboardingResponseDTO;
import com.peopleops.service.OffboardingService;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Offboarding", description = "Employee exit clearance and offboarding APIs")
@RestController
@RequestMapping("/api/offboardings")
public class OffboardingController {

    private final OffboardingService offboardingService;

    public OffboardingController(OffboardingService offboardingService) {
        this.offboardingService = offboardingService;
    }

    @PostMapping
    public ResponseEntity<OffboardingResponseDTO> initiateOffboarding(
            @Valid @RequestBody OffboardingRequestDTO request) {
        OffboardingResponseDTO response = offboardingService.initiateOffboarding(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OffboardingResponseDTO>> getAllOffboardings() {
        List<OffboardingResponseDTO> list = offboardingService.getAllOffboardings();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OffboardingResponseDTO> getOffboardingById(@PathVariable Long id) {
        OffboardingResponseDTO response = offboardingService.getOffboardingById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OffboardingResponseDTO> updateOffboarding(
            @PathVariable Long id,
            @Valid @RequestBody OffboardingRequestDTO request) {
        OffboardingResponseDTO response = offboardingService.updateOffboarding(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffboarding(@PathVariable Long id) {
        offboardingService.deleteOffboarding(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<OffboardingResponseDTO> completeOffboarding(@PathVariable Long id) {
        OffboardingResponseDTO response = offboardingService.completeOffboarding(id);
        return ResponseEntity.ok(response);
    }
}
