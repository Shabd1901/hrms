package com.peopleops.repository;

import com.peopleops.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {
    boolean existsByTitle(String title);
}
