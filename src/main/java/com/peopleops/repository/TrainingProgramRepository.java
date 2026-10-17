package com.peopleops.repository;

import com.peopleops.entity.TrainingProgram;
import com.peopleops.enums.TrainingStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {
    List<TrainingProgram> findByStatus(TrainingStatus status);
}
