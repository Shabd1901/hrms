package com.peopleops.repository;

import com.peopleops.entity.Offboarding;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffboardingRepository extends JpaRepository<Offboarding, Long> {
    Optional<Offboarding> findByEmployeeId(Long employeeId);
    boolean existsByEmployeeId(Long employeeId);
}
