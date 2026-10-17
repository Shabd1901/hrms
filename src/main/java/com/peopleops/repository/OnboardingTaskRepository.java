package com.peopleops.repository;

import com.peopleops.entity.OnboardingTask;
import com.peopleops.enums.OnboardingTaskStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardingTaskRepository extends JpaRepository<OnboardingTask, Long> {
    List<OnboardingTask> findByEmployeeId(Long employeeId);
    void deleteByEmployeeId(Long employeeId);
    boolean existsByEmployeeIdAndStatus(Long employeeId, OnboardingTaskStatus status);
    long countByEmployeeId(Long employeeId);
    long countByEmployeeIdAndStatus(Long employeeId, OnboardingTaskStatus status);
}
