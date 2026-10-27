package com.peopleops.repository;

import com.peopleops.entity.Employee;
import com.peopleops.enums.EmployeeStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmployeeCode(String employeeCode);
    boolean existsByDepartmentId(Long departmentId);
    boolean existsByDesignationId(Long designationId);
    boolean existsByCandidateId(Long candidateId);
    Optional<Employee> findByCandidateId(Long candidateId);

    List<Employee> findByDepartmentIdAndStatus(Long departmentId, EmployeeStatus status);
    List<Employee> findByDepartmentId(Long departmentId);
    List<Employee> findByStatus(EmployeeStatus status);

    @Query("SELECT MAX(e.id) FROM Employee e")
    Optional<Long> findMaxId();
}
