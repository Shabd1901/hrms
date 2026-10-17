package com.peopleops.repository;

import com.peopleops.entity.PerformanceReview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    boolean existsByEmployeeIdAndReviewPeriod(Long employeeId, String reviewPeriod);
    List<PerformanceReview> findByEmployeeId(Long employeeId);
}
