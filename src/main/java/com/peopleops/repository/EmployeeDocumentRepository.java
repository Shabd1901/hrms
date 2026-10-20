package com.peopleops.repository;

import com.peopleops.entity.EmployeeDocument;
import com.peopleops.enums.DocumentType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Long> {
    List<EmployeeDocument> findByEmployeeId(Long employeeId);
    boolean existsByEmployeeIdAndDocumentTypeAndVerifiedTrue(Long employeeId, DocumentType documentType);
    void deleteByEmployeeId(Long employeeId);
}
