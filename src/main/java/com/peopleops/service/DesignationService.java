package com.peopleops.service;

import com.peopleops.dto.DesignationRequestDTO;
import com.peopleops.dto.DesignationResponseDTO;
import com.peopleops.entity.Designation;
import com.peopleops.repository.DesignationRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DesignationService {

    private final DesignationRepository designationRepository;

    public DesignationService(DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }

    public DesignationResponseDTO createDesignation(DesignationRequestDTO request) {
        if (designationRepository.existsByTitle(request.getTitle())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Designation title already exists: " + request.getTitle());
        }

        Designation designation = toEntity(request);
        Designation saved = designationRepository.save(designation);
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + id));
        return toResponseDTO(designation);
    }

    public DesignationResponseDTO updateDesignation(Long id, DesignationRequestDTO request) {
        Designation designation = designationRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + id));

        if (!designation.getTitle().equals(request.getTitle()) && designationRepository.existsByTitle(request.getTitle())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Designation title already exists: " + request.getTitle());
        }

        designation.setTitle(request.getTitle());
        designation.setGradeLevel(request.getGradeLevel());
        designation.setDescription(request.getDescription());
        Designation updated = designationRepository.save(designation);
        return toResponseDTO(updated);
    }

    public void deleteDesignation(Long id) {
        Designation designation = designationRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + id));
        designationRepository.delete(designation);
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
