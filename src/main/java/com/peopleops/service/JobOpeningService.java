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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JobOpeningService {

    private static final Logger log = LoggerFactory.getLogger(JobOpeningService.class);

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
        if (request.getDepartmentId() == null) {
            log.warn("Job opening creation failed: departmentId is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department id is required");
        }
        if (request.getDesignationId() == null) {
            log.warn("Job opening creation failed: designationId is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Designation id is required");
        }
        if (!departmentRepository.existsById(request.getDepartmentId())) {
            log.warn("Job opening creation failed: department id {} not found", request.getDepartmentId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + request.getDepartmentId());
        }
        if (!designationRepository.existsById(request.getDesignationId())) {
            log.warn("Job opening creation failed: designation id {} not found", request.getDesignationId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + request.getDesignationId());
        }

        JobOpening opening = toEntity(request);
        opening.setStatus(JobOpeningStatus.OPEN);
        JobOpening saved = jobOpeningRepository.save(opening);
        log.info("Created job opening with id: {} and title: '{}'", saved.getId(), saved.getTitle());
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
            .orElseThrow(() -> {
                log.warn("Job opening lookup failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found with id: " + id);
            });
        return toResponseDTO(opening);
    }

    public JobOpeningResponseDTO updateJobOpening(Long id, JobOpeningRequestDTO request) {
        JobOpening opening = jobOpeningRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Job opening update failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found with id: " + id);
            });

        if (!departmentRepository.existsById(request.getDepartmentId())) {
            log.warn("Job opening update failed for id {}: department id {} not found", id, request.getDepartmentId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + request.getDepartmentId());
        }
        if (!designationRepository.existsById(request.getDesignationId())) {
            log.warn("Job opening update failed for id {}: designation id {} not found", id, request.getDesignationId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + request.getDesignationId());
        }

        opening.setTitle(request.getTitle());
        opening.setDepartmentId(request.getDepartmentId());
        opening.setDesignationId(request.getDesignationId());
        opening.setOpenPositions(request.getOpenPositions());
        opening.setPostedDate(request.getPostedDate());

        JobOpening updated = jobOpeningRepository.save(opening);
        log.info("Updated job opening id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteJobOpening(Long id) {
        JobOpening opening = jobOpeningRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Job opening delete failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found with id: " + id);
            });
        jobOpeningRepository.delete(opening);
        log.info("Deleted job opening id: {}", id);
    }

    public JobOpeningResponseDTO updateStatus(Long id, JobOpeningStatus status) {
        JobOpening opening = jobOpeningRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Job opening status patch failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found with id: " + id);
            });
        opening.setStatus(status);
        JobOpening updated = jobOpeningRepository.save(opening);
        log.info("Updated job opening id: {} status to {}", id, status);
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
