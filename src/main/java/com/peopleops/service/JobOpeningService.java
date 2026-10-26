package com.peopleops.service;

import com.peopleops.dto.JobOpeningRequestDTO;
import com.peopleops.dto.JobOpeningResponseDTO;
import com.peopleops.entity.Department;
import com.peopleops.entity.Designation;
import com.peopleops.entity.JobOpening;
import com.peopleops.enums.JobOpeningStatus;
import com.peopleops.exception.BusinessRuleException;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.DepartmentRepository;
import com.peopleops.repository.DesignationRepository;
import com.peopleops.repository.JobOpeningRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
            throw new BusinessRuleException("Department id is required");
        }
        if (request.getDesignationId() == null) {
            log.warn("Job opening creation failed: designationId is null");
            throw new BusinessRuleException("Designation id is required");
        }
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> {
                log.warn("Job opening creation failed: department id {} not found", request.getDepartmentId());
                return new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId());
            });
        Designation designation = designationRepository.findById(request.getDesignationId())
            .orElseThrow(() -> {
                log.warn("Job opening creation failed: designation id {} not found", request.getDesignationId());
                return new ResourceNotFoundException("Designation not found with id: " + request.getDesignationId());
            });

        JobOpening opening = toEntity(request);
        opening.setDepartment(department);
        opening.setDesignation(designation);
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
                return new ResourceNotFoundException("Job opening not found with id: " + id);
            });
        return toResponseDTO(opening);
    }

    public JobOpeningResponseDTO updateJobOpening(Long id, JobOpeningRequestDTO request) {
        JobOpening opening = jobOpeningRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Job opening update failed: id {} not found", id);
                return new ResourceNotFoundException("Job opening not found with id: " + id);
            });

        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> {
                log.warn("Job opening update failed for id {}: department id {} not found", id, request.getDepartmentId());
                return new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId());
            });
        Designation designation = designationRepository.findById(request.getDesignationId())
            .orElseThrow(() -> {
                log.warn("Job opening update failed for id {}: designation id {} not found", id, request.getDesignationId());
                return new ResourceNotFoundException("Designation not found with id: " + request.getDesignationId());
            });

        opening.setTitle(request.getTitle());
        opening.setDepartment(department);
        opening.setDesignation(designation);
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
                return new ResourceNotFoundException("Job opening not found with id: " + id);
            });
        jobOpeningRepository.delete(opening);
        log.info("Deleted job opening id: {}", id);
    }

    public JobOpeningResponseDTO updateStatus(Long id, JobOpeningStatus status) {
        JobOpening opening = jobOpeningRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Job opening status patch failed: id {} not found", id);
                return new ResourceNotFoundException("Job opening not found with id: " + id);
            });
        opening.setStatus(status);
        JobOpening updated = jobOpeningRepository.save(opening);
        log.info("Updated job opening id: {} status to {}", id, status);
        return toResponseDTO(updated);
    }

    private JobOpening toEntity(JobOpeningRequestDTO dto) {
        JobOpening opening = new JobOpening();
        opening.setTitle(dto.getTitle());
        opening.setOpenPositions(dto.getOpenPositions());
        opening.setPostedDate(dto.getPostedDate() != null ? dto.getPostedDate() : LocalDate.now());
        return opening;
    }

    private JobOpeningResponseDTO toResponseDTO(JobOpening entity) {
        return new JobOpeningResponseDTO(
            entity.getId(),
            entity.getTitle(),
            entity.getDepartment() != null ? entity.getDepartment().getId() : null,
            entity.getDesignation() != null ? entity.getDesignation().getId() : null,
            entity.getOpenPositions(),
            entity.getPostedDate(),
            entity.getStatus()
        );
    }
}
