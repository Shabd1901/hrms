package com.peopleops.controller;
import com.peopleops.dto.DepartmentRequestDTO;
import com.peopleops.dto.DepartmentResponseDTO;
import com.peopleops.service.DepartmentService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@RequestBody DepartmentRequestDTO request) {
        DepartmentResponseDTO response = departmentService.createDepartment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDTO response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(response);
    }
}