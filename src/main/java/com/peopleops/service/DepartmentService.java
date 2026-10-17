package com.peopleops.service;

import com.peopleops.dto.DepartmentRequestDTO;
import com.peopleops.dto.DepartmentResponseDTO;
import com.peopleops.entity.Department;
import com.peopleops.repository.DepartmentRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department name already exists: " + request.getName());
        }
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department code already exists: " + request.getCode());
        }

        Department department = toEntity(request);
        Department saved = departmentRepository.save(department);
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + id));
        return toResponseDTO(department);
    }

    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + id));

        if (!department.getName().equals(request.getName()) && departmentRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department name already exists: " + request.getName());
        }
        if (!department.getCode().equals(request.getCode()) && departmentRepository.existsByCode(request.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department code already exists: " + request.getCode());
        }

        department.setName(request.getName());
        department.setCode(request.getCode());
        department.setDescription(request.getDescription());
        Department updated = departmentRepository.save(department);
        return toResponseDTO(updated);
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + id));
        departmentRepository.delete(department);
    }

    public DepartmentResponseDTO updateDepartmentActive(Long id, Boolean active) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + id));
        department.setActive(active);
        Department updated = departmentRepository.save(department);
        return toResponseDTO(updated);
    }

    private Department toEntity(DepartmentRequestDTO dto) {
        Department department = new Department();
        department.setName(dto.getName());
        department.setCode(dto.getCode());
        department.setDescription(dto.getDescription());
        department.setActive(true);
        return department;
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
