package com.peopleops.service;

import com.peopleops.dto.PerformanceReviewRequestDTO;
import com.peopleops.dto.PerformanceReviewResponseDTO;
import com.peopleops.entity.Employee;
import com.peopleops.entity.PerformanceReview;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.enums.ReviewStatus;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.PerformanceReviewRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PerformanceReviewService {

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee id is required");
        }
        if (request.getReviewerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reviewer id is required");
        }
        if (!employeeRepository.existsById(request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId());
        }

        Employee reviewer = employeeRepository.findById(request.getReviewerId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reviewer not found with id: " + request.getReviewerId()));

        if (reviewer.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reviewer must be an active or working employee");
        }

        if (request.getReviewerId().equals(request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reviewer cannot be the employee being reviewed");
        }

        if (request.getRating() != null && (request.getRating() < 1 || request.getRating() > 5)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        if (performanceReviewRepository.existsByEmployeeIdAndReviewPeriod(request.getEmployeeId(), request.getReviewPeriod())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Review already exists for this employee and period: " + request.getReviewPeriod());
        }

        PerformanceReview review = toEntity(request);
        review.setStatus(ReviewStatus.DRAFT);
        PerformanceReview saved = performanceReviewRepository.save(review);
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Performance review not found with id: " + id));
        return toResponseDTO(review);
    }

    public PerformanceReviewResponseDTO updatePerformanceReview(Long id, PerformanceReviewRequestDTO request) {
        PerformanceReview review = performanceReviewRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Performance review not found with id: " + id));

        if (review.getStatus() != ReviewStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A SUBMITTED review cannot be edited");
        }

        if (!employeeRepository.existsById(request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId());
        }

        Employee reviewer = employeeRepository.findById(request.getReviewerId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reviewer not found with id: " + request.getReviewerId()));

        if (reviewer.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reviewer must be an active or working employee");
        }

        if (request.getReviewerId().equals(request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reviewer cannot be the employee being reviewed");
        }

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        if ((!review.getEmployeeId().equals(request.getEmployeeId()) || !review.getReviewPeriod().equals(request.getReviewPeriod()))
                && performanceReviewRepository.existsByEmployeeIdAndReviewPeriod(request.getEmployeeId(), request.getReviewPeriod())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Review already exists for this employee and period: " + request.getReviewPeriod());
        }

        review.setEmployeeId(request.getEmployeeId());
        review.setReviewerId(request.getReviewerId());
        review.setReviewPeriod(request.getReviewPeriod());
        review.setRating(request.getRating());
        review.setComments(request.getComments());
        review.setReviewDate(request.getReviewDate());

        PerformanceReview updated = performanceReviewRepository.save(review);
        return toResponseDTO(updated);
    }

    public void deletePerformanceReview(Long id) {
        PerformanceReview review = performanceReviewRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Performance review not found with id: " + id));

        if (review.getStatus() != ReviewStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A SUBMITTED review cannot be deleted");
        }

        performanceReviewRepository.delete(review);
    }

    public PerformanceReviewResponseDTO submitPerformanceReview(Long id) {
        PerformanceReview review = performanceReviewRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Performance review not found with id: " + id));

        review.setStatus(ReviewStatus.SUBMITTED);
        PerformanceReview updated = performanceReviewRepository.save(review);
        return toResponseDTO(updated);
    }

    private PerformanceReview toEntity(PerformanceReviewRequestDTO dto) {
        PerformanceReview pr = new PerformanceReview();
        pr.setEmployeeId(dto.getEmployeeId());
        pr.setReviewerId(dto.getReviewerId());
        pr.setReviewPeriod(dto.getReviewPeriod());
        pr.setRating(dto.getRating());
        pr.setComments(dto.getComments());
        pr.setReviewDate(dto.getReviewDate());
        return pr;
    }

    private PerformanceReviewResponseDTO toResponseDTO(PerformanceReview entity) {
        return new PerformanceReviewResponseDTO(
            entity.getId(),
            entity.getEmployeeId(),
            entity.getReviewerId(),
            entity.getReviewPeriod(),
            entity.getRating(),
            entity.getComments(),
            entity.getStatus(),
            entity.getReviewDate()
        );
    }
}
