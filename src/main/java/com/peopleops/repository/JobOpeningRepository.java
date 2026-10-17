package com.peopleops.repository;

import com.peopleops.entity.JobOpening;
import com.peopleops.enums.JobOpeningStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOpeningRepository extends JpaRepository<JobOpening, Long> {
    List<JobOpening> findByStatus(JobOpeningStatus status);
    List<JobOpening> findByDepartmentId(Long departmentId);
    boolean existsByDepartmentId(Long departmentId);
    boolean existsByDesignationId(Long designationId);
}
