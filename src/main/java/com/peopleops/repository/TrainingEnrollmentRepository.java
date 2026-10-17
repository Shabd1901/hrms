package com.peopleops.repository;

import com.peopleops.entity.TrainingEnrollment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingEnrollmentRepository extends JpaRepository<TrainingEnrollment, Long> {
    boolean existsByTrainingProgramIdAndEmployeeId(Long trainingProgramId, Long employeeId);
    List<TrainingEnrollment> findByEmployeeId(Long employeeId);
    List<TrainingEnrollment> findByTrainingProgramId(Long trainingProgramId);
}
