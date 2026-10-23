package com.peopleops.service;

import com.peopleops.dto.DepartmentRequestDTO;
import com.peopleops.dto.DepartmentResponseDTO;
import com.peopleops.entity.Department;
import com.peopleops.repository.DepartmentRepository;
import com.peopleops.repository.EmployeeRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO request) {
        if (departmentRepository.existsByName(request.getName())) {
            log.warn("Department creation failed: duplicate name '{}'", request.getName());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department name already exists: " + request.getName());
        }
        if (departmentRepository.existsByCode(request.getCode())) {
            log.warn("Department creation failed: duplicate code '{}'", request.getCode());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department code already exists: " + request.getCode());
        }

        Department department = toEntity(request);
        Department saved = departmentRepository.save(department);
        log.info("Created department with id: {} and code: '{}'", saved.getId(), saved.getCode());
        return toResponseDTO(saved);
    }

    public List<DepartmentResponseDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentResponseDTO> responses = new ArrayList<>();
        for (Department dept : departments) {
            responses.add(toResponseDTO(dept));
        }
        return responses;
    }

    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Department lookup failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + id);
            });
        return toResponseDTO(department);
    }

    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Department update failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + id);
            });

        if (!department.getName().equals(request.getName()) && departmentRepository.existsByName(request.getName())) {
            log.warn("Department update failed for id {}: duplicate name '{}'", id, request.getName());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department name already exists: " + request.getName());
        }
        if (!department.getCode().equals(request.getCode()) && departmentRepository.existsByCode(request.getCode())) {
            log.warn("Department update failed for id {}: duplicate code '{}'", id, request.getCode());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department code already exists: " + request.getCode());
        }

        department.setName(request.getName());
        department.setCode(request.getCode());
        department.setDescription(request.getDescription());

        Department updated = departmentRepository.save(department);
        log.info("Updated department id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Department delete failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + id);
            });

        if (employeeRepository.existsByDepartmentId(id)) {
            log.warn("Department delete failed: id {} has assigned employees", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete department with assigned employees");
        }

        departmentRepository.delete(department);
        log.info("Deleted department id: {}", id);
    }

    public DepartmentResponseDTO updateDepartmentActive(Long id, Boolean active) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Department status patch failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + id);
            });

        department.setActive(active);
        Department updated = departmentRepository.save(department);
        log.info("Patched department id: {} active status to {}", id, active);
        return toResponseDTO(updated);
    }

    private Department toEntity(DepartmentRequestDTO dto) {
        Department dept = new Department();
        dept.setName(dto.getName());
        dept.setCode(dto.getCode());
        dept.setDescription(dto.getDescription());
        dept.setActive(true);
        return dept;
    }

    private DepartmentResponseDTO toResponseDTO(Department entity) {
        return new DepartmentResponseDTO(
            entity.getId(),
            entity.getName(),
            entity.getCode(),
            entity.getDescription(),
            entity.getActive()
        );
    }
}
