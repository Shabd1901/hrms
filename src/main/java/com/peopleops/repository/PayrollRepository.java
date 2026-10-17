package com.peopleops.repository;

import com.peopleops.entity.Payroll;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    Optional<Payroll> findByEmployeeIdAndPayMonthAndPayYear(Long employeeId, Integer payMonth, Integer payYear);
    boolean existsByEmployeeIdAndPayMonthAndPayYear(Long employeeId, Integer payMonth, Integer payYear);
    List<Payroll> findByEmployeeId(Long employeeId);
}
