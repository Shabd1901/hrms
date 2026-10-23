package com.peopleops.service;

import com.peopleops.dto.DesignationRequestDTO;
import com.peopleops.dto.DesignationResponseDTO;
import com.peopleops.entity.Designation;
import com.peopleops.repository.DesignationRepository;
import com.peopleops.repository.EmployeeRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DesignationService {

    private static final Logger log = LoggerFactory.getLogger(DesignationService.class);

    private final DesignationRepository designationRepository;
    private final EmployeeRepository employeeRepository;

    public DesignationService(
            DesignationRepository designationRepository,
            EmployeeRepository employeeRepository) {
        this.designationRepository = designationRepository;
        this.employeeRepository = employeeRepository;
    }

    public DesignationResponseDTO createDesignation(DesignationRequestDTO request) {
        if (designationRepository.existsByTitle(request.getTitle())) {
            log.warn("Designation creation failed: duplicate title '{}'", request.getTitle());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Designation title already exists: " + request.getTitle());
        }

        Designation designation = toEntity(request);
        Designation saved = designationRepository.save(designation);
        log.info("Created designation with id: {} and title: '{}'", saved.getId(), saved.getTitle());
        return toResponseDTO(saved);
    }

    public List<DesignationResponseDTO> getAllDesignations() {
        List<Designation> designations = designationRepository.findAll();
        List<DesignationResponseDTO> responses = new ArrayList<>();
        for (Designation desig : designations) {
            responses.add(toResponseDTO(desig));
        }
        return responses;
    }

    public DesignationResponseDTO getDesignationById(Long id) {
        Designation designation = designationRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Designation lookup failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + id);
            });
        return toResponseDTO(designation);
    }

    public DesignationResponseDTO updateDesignation(Long id, DesignationRequestDTO request) {
        Designation designation = designationRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Designation update failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + id);
            });

        if (!designation.getTitle().equals(request.getTitle()) && designationRepository.existsByTitle(request.getTitle())) {
            log.warn("Designation update failed for id {}: duplicate title '{}'", id, request.getTitle());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Designation title already exists: " + request.getTitle());
        }

        designation.setTitle(request.getTitle());
        designation.setGradeLevel(request.getGradeLevel());
        designation.setDescription(request.getDescription());

        Designation updated = designationRepository.save(designation);
        log.info("Updated designation id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteDesignation(Long id) {
        Designation designation = designationRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Designation delete failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + id);
            });

        if (employeeRepository.existsByDesignationId(id)) {
            log.warn("Designation delete failed: id {} has assigned employees", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete designation with assigned employees");
        }

        designationRepository.delete(designation);
        log.info("Deleted designation id: {}", id);
    }

    private Designation toEntity(DesignationRequestDTO dto) {
        Designation designation = new Designation();
        designation.setTitle(dto.getTitle());
        designation.setGradeLevel(dto.getGradeLevel());
        designation.setDescription(dto.getDescription());
        return designation;
    }

    private DesignationResponseDTO toResponseDTO(Designation entity) {
        return new DesignationResponseDTO(
            entity.getId(),
            entity.getTitle(),
            entity.getGradeLevel(),
            entity.getDescription()
        );
    }
}
