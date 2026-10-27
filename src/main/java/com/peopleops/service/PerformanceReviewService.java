package com.peopleops.service;

import com.peopleops.dto.PerformanceReviewRequestDTO;
import com.peopleops.dto.PerformanceReviewResponseDTO;
import com.peopleops.entity.Employee;
import com.peopleops.entity.PerformanceReview;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.enums.ReviewStatus;
import com.peopleops.exception.BusinessRuleException;
import com.peopleops.exception.DuplicateResourceException;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.PerformanceReviewRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PerformanceReviewService {

    private static final Logger log = LoggerFactory.getLogger(PerformanceReviewService.class);

    private final PerformanceReviewRepository performanceReviewRepository;
    private final EmployeeRepository employeeRepository;

    public PerformanceReviewService(
            PerformanceReviewRepository performanceReviewRepository,
            EmployeeRepository employeeRepository) {
        this.performanceReviewRepository = performanceReviewRepository;
        this.employeeRepository = employeeRepository;
    }

    public PerformanceReviewResponseDTO createPerformanceReview(PerformanceReviewRequestDTO request) {
        if (request.getEmployeeId() == null) {
            log.warn("Performance review creation failed: employeeId is null");
            throw new BusinessRuleException("Employee id is required");
        }
        if (request.getReviewerId() == null) {
            log.warn("Performance review creation failed: reviewerId is null");
            throw new BusinessRuleException("Reviewer id is required");
        }
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> {
                log.warn("Performance review creation failed: employee id {} not found", request.getEmployeeId());
                return new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId());
            });

        Employee reviewer = employeeRepository.findById(request.getReviewerId())
            .orElseThrow(() -> {
                log.warn("Performance review creation failed: reviewer id {} not found", request.getReviewerId());
                return new ResourceNotFoundException("Reviewer not found with id: " + request.getReviewerId());
            });

        if (reviewer.getStatus() == EmployeeStatus.EXITED) {
            log.warn("Performance review creation failed: reviewer id {} is EXITED", reviewer.getId());
            throw new BusinessRuleException("Reviewer must be an active or working employee");
        }

        if (request.getReviewerId().equals(request.getEmployeeId())) {
            log.warn("Performance review creation failed: reviewer id {} cannot review themselves", request.getReviewerId());
            throw new BusinessRuleException("Reviewer cannot be the employee being reviewed");
        }

        if (request.getRating() != null && (request.getRating() < 1 || request.getRating() > 5)) {
            log.warn("Performance review creation failed: invalid rating {}", request.getRating());
            throw new BusinessRuleException("Rating must be between 1 and 5");
        }

        if (performanceReviewRepository.existsByEmployeeIdAndReviewPeriod(request.getEmployeeId(), request.getReviewPeriod())) {
            log.warn("Performance review creation failed: duplicate review for employee id {} in period '{}'", request.getEmployeeId(), request.getReviewPeriod());
            throw new DuplicateResourceException("Review already exists for this employee and period: " + request.getReviewPeriod());
        }

        PerformanceReview review = toEntity(request);
        review.setEmployee(employee);
        review.setReviewer(reviewer);
        review.setStatus(ReviewStatus.DRAFT);
        PerformanceReview saved = performanceReviewRepository.save(review);
        log.info("Created performance review draft id: {} for employee id: {} for period '{}'", saved.getId(), saved.getEmployee() != null ? saved.getEmployee().getId() : null, saved.getReviewPeriod());
        return toResponseDTO(saved);
    }

    public List<PerformanceReviewResponseDTO> getAllPerformanceReviews(Long employeeId) {
        List<PerformanceReview> list = (employeeId != null)
            ? performanceReviewRepository.findByEmployeeId(employeeId)
            : performanceReviewRepository.findAll();

        List<PerformanceReviewResponseDTO> responses = new ArrayList<>();
        for (PerformanceReview pr : list) {
            responses.add(toResponseDTO(pr));
        }
        return responses;
    }

    public PerformanceReviewResponseDTO getPerformanceReviewById(Long id) {
        PerformanceReview review = performanceReviewRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Performance review lookup failed: id {} not found", id);
                return new ResourceNotFoundException("Performance review not found with id: " + id);
            });
        return toResponseDTO(review);
    }

    public PerformanceReviewResponseDTO updatePerformanceReview(Long id, PerformanceReviewRequestDTO request) {
        PerformanceReview review = performanceReviewRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Performance review update failed: id {} not found", id);
                return new ResourceNotFoundException("Performance review not found with id: " + id);
            });

        if (review.getStatus() != ReviewStatus.DRAFT) {
            log.warn("Performance review update failed for id {}: cannot update SUBMITTED review", id);
            throw new BusinessRuleException("A SUBMITTED review cannot be edited");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> {
                log.warn("Performance review update failed for id {}: employee id {} not found", id, request.getEmployeeId());
                return new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId());
            });

        Employee reviewer = employeeRepository.findById(request.getReviewerId())
            .orElseThrow(() -> {
                log.warn("Performance review update failed for id {}: reviewer id {} not found", id, request.getReviewerId());
                return new ResourceNotFoundException("Reviewer not found with id: " + request.getReviewerId());
            });

        if (reviewer.getStatus() == EmployeeStatus.EXITED) {
            log.warn("Performance review update failed for id {}: reviewer id {} is EXITED", id, reviewer.getId());
            throw new BusinessRuleException("Reviewer must be an active or working employee");
        }

        if (request.getReviewerId().equals(request.getEmployeeId())) {
            log.warn("Performance review update failed for id {}: self-review not allowed", id);
            throw new BusinessRuleException("Reviewer cannot be the employee being reviewed");
        }

        if (request.getRating() < 1 || request.getRating() > 5) {
            log.warn("Performance review update failed for id {}: invalid rating {}", id, request.getRating());
            throw new BusinessRuleException("Rating must be between 1 and 5");
        }

        Long currentEmpId = review.getEmployee() != null ? review.getEmployee().getId() : null;
        if ((!request.getEmployeeId().equals(currentEmpId) || !review.getReviewPeriod().equals(request.getReviewPeriod()))
                && performanceReviewRepository.existsByEmployeeIdAndReviewPeriod(request.getEmployeeId(), request.getReviewPeriod())) {
            log.warn("Performance review update failed for id {}: duplicate review for period '{}'", id, request.getReviewPeriod());
            throw new DuplicateResourceException("Review already exists for this employee and period: " + request.getReviewPeriod());
        }

        review.setEmployee(employee);
        review.setReviewer(reviewer);
        review.setReviewPeriod(request.getReviewPeriod());
        review.setRating(request.getRating());
        review.setComments(request.getComments());
        review.setReviewDate(request.getReviewDate());

        PerformanceReview updated = performanceReviewRepository.save(review);
        log.info("Updated performance review id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deletePerformanceReview(Long id) {
        PerformanceReview review = performanceReviewRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Performance review delete failed: id {} not found", id);
                return new ResourceNotFoundException("Performance review not found with id: " + id);
            });

        if (review.getStatus() != ReviewStatus.DRAFT) {
            log.warn("Performance review delete failed for id {}: cannot delete SUBMITTED review", id);
            throw new BusinessRuleException("A SUBMITTED review cannot be deleted");
        }

        performanceReviewRepository.delete(review);
        log.info("Deleted performance review id: {}", id);
    }

    public PerformanceReviewResponseDTO submitPerformanceReview(Long id) {
        PerformanceReview review = performanceReviewRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Performance review submit failed: id {} not found", id);
                return new ResourceNotFoundException("Performance review not found with id: " + id);
            });

        review.setStatus(ReviewStatus.SUBMITTED);
        PerformanceReview updated = performanceReviewRepository.save(review);
        log.info("Submitted performance review id: {}", id);
        return toResponseDTO(updated);
    }

    private PerformanceReview toEntity(PerformanceReviewRequestDTO dto) {
        PerformanceReview pr = new PerformanceReview();
        pr.setReviewPeriod(dto.getReviewPeriod());
        pr.setRating(dto.getRating());
        pr.setComments(dto.getComments());
        pr.setReviewDate(dto.getReviewDate());
        return pr;
    }

    private PerformanceReviewResponseDTO toResponseDTO(PerformanceReview entity) {
        return new PerformanceReviewResponseDTO(
            entity.getId(),
            entity.getEmployee() != null ? entity.getEmployee().getId() : null,
            entity.getReviewer() != null ? entity.getReviewer().getId() : null,
            entity.getReviewPeriod(),
            entity.getRating(),
            entity.getComments(),
            entity.getStatus(),
            entity.getReviewDate()
        );
    }
}
