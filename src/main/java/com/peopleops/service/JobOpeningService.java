package com.peopleops.service;

import com.peopleops.dto.JobOpeningRequestDTO;
import com.peopleops.dto.JobOpeningResponseDTO;
import com.peopleops.entity.JobOpening;
import com.peopleops.enums.JobOpeningStatus;
import com.peopleops.repository.DepartmentRepository;
import com.peopleops.repository.DesignationRepository;
import com.peopleops.repository.JobOpeningRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JobOpeningService {

    private final JobOpeningRepository jobOpeningRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;

    public JobOpeningService(
            JobOpeningRepository jobOpeningRepository,
            DepartmentRepository departmentRepository,
            DesignationRepository designationRepository) {
        this.jobOpeningRepository = jobOpeningRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
    }

    public JobOpeningResponseDTO createJobOpening(JobOpeningRequestDTO request) {
        if (!departmentRepository.existsById(request.getDepartmentId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + request.getDepartmentId());
        }
        if (!designationRepository.existsById(request.getDesignationId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + request.getDesignationId());
        }

        JobOpening opening = toEntity(request);
        opening.setStatus(JobOpeningStatus.OPEN);
        JobOpening saved = jobOpeningRepository.save(opening);
        return toResponseDTO(saved);
    }

    public List<JobOpeningResponseDTO> getAllJobOpenings(JobOpeningStatus status) {
        List<JobOpening> openings = (status != null) 
            ? jobOpeningRepository.findByStatus(status) 
            : jobOpeningRepository.findAll();

        List<JobOpeningResponseDTO> responses = new ArrayList<>();
        for (JobOpening opening : openings) {
            responses.add(toResponseDTO(opening));
        }
        return responses;
    }

    public JobOpeningResponseDTO getJobOpeningById(Long id) {
        JobOpening opening = jobOpeningRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found with id: " + id));
        return toResponseDTO(opening);
    }

    public JobOpeningResponseDTO updateJobOpening(Long id, JobOpeningRequestDTO request) {
        JobOpening opening = jobOpeningRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found with id: " + id));

        if (!departmentRepository.existsById(request.getDepartmentId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + request.getDepartmentId());
        }
        if (!designationRepository.existsById(request.getDesignationId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + request.getDesignationId());
        }

        opening.setTitle(request.getTitle());
        opening.setDepartmentId(request.getDepartmentId());
        opening.setDesignationId(request.getDesignationId());
        opening.setOpenPositions(request.getOpenPositions());
        opening.setPostedDate(request.getPostedDate());

        JobOpening updated = jobOpeningRepository.save(opening);
        return toResponseDTO(updated);
    }

    public void deleteJobOpening(Long id) {
        JobOpening opening = jobOpeningRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found with id: " + id));
        jobOpeningRepository.delete(opening);
    }

    public JobOpeningResponseDTO updateStatus(Long id, JobOpeningStatus status) {
        JobOpening opening = jobOpeningRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found with id: " + id));
        opening.setStatus(status);
        JobOpening updated = jobOpeningRepository.save(opening);
        return toResponseDTO(updated);
    }

    private JobOpening toEntity(JobOpeningRequestDTO dto) {
        JobOpening opening = new JobOpening();
        opening.setTitle(dto.getTitle());
        opening.setDepartmentId(dto.getDepartmentId());
        opening.setDesignationId(dto.getDesignationId());
        opening.setOpenPositions(dto.getOpenPositions());
        opening.setPostedDate(dto.getPostedDate());
        return opening;
    }

    private JobOpeningResponseDTO toResponseDTO(JobOpening entity) {
        return new JobOpeningResponseDTO(
            entity.getId(),
            entity.getTitle(),
            entity.getDepartmentId(),
            entity.getDesignationId(),
            entity.getOpenPositions(),
            entity.getPostedDate(),
            entity.getStatus()
        );
    }
}
