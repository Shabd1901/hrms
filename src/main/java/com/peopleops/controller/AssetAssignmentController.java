package com.peopleops.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.peopleops.dto.AssetAssignmentRequestDTO;
import com.peopleops.dto.AssetAssignmentResponseDTO;
import com.peopleops.service.AssetAssignmentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Asset Assignments", description = "Asset allocation and return tracking APIs")
@RestController
@RequestMapping("/api/asset-assignments")
public class AssetAssignmentController {

    private final AssetAssignmentService assetAssignmentService;

    public AssetAssignmentController(AssetAssignmentService assetAssignmentService) {
        this.assetAssignmentService = assetAssignmentService;
    }

    @PostMapping
    public ResponseEntity<AssetAssignmentResponseDTO> assignAsset(
            @Valid @RequestBody AssetAssignmentRequestDTO request) {
        AssetAssignmentResponseDTO response = assetAssignmentService.assignAsset(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AssetAssignmentResponseDTO>> getAllAssignments(
            @RequestParam(required = false) Long employeeId) {
        List<AssetAssignmentResponseDTO> list = assetAssignmentService.getAllAssignments(employeeId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetAssignmentResponseDTO> getAssignmentById(@PathVariable Long id) {
        AssetAssignmentResponseDTO response = assetAssignmentService.getAssignmentById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<AssetAssignmentResponseDTO> returnAsset(@PathVariable Long id) {
        AssetAssignmentResponseDTO response = assetAssignmentService.returnAsset(id);
        return ResponseEntity.ok(response);
    }
}
