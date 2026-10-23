package com.peopleops.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.peopleops.dto.EmployeeDocumentRequestDTO;
import com.peopleops.dto.EmployeeDocumentResponseDTO;
import com.peopleops.service.EmployeeDocumentService;
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

@Tag(name = "Employee Documents", description = "Employee document upload and verification APIs")
@RestController
@RequestMapping("/api/employee-documents")
public class EmployeeDocumentController {

    private final EmployeeDocumentService employeeDocumentService;

    public EmployeeDocumentController(EmployeeDocumentService employeeDocumentService) {
        this.employeeDocumentService = employeeDocumentService;
    }

    @PostMapping
    public ResponseEntity<EmployeeDocumentResponseDTO> createDocument(
            @Valid @RequestBody EmployeeDocumentRequestDTO request) {
        EmployeeDocumentResponseDTO response = employeeDocumentService.createDocument(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDocumentResponseDTO>> getAllDocuments(
            @RequestParam(required = false) Long employeeId) {
        List<EmployeeDocumentResponseDTO> list = employeeDocumentService.getAllDocuments(employeeId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDocumentResponseDTO> getDocumentById(@PathVariable Long id) {
        EmployeeDocumentResponseDTO response = employeeDocumentService.getDocumentById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDocumentResponseDTO> updateDocument(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDocumentRequestDTO request) {
        EmployeeDocumentResponseDTO response = employeeDocumentService.updateDocument(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        employeeDocumentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<EmployeeDocumentResponseDTO> verifyDocument(@PathVariable Long id) {
        EmployeeDocumentResponseDTO response = employeeDocumentService.verifyDocument(id);
        return ResponseEntity.ok(response);
    }
}
