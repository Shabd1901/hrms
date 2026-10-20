package com.peopleops.repository;

import com.peopleops.entity.AssetAssignment;
import com.peopleops.enums.AssignmentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetAssignmentRepository extends JpaRepository<AssetAssignment, Long> {
    List<AssetAssignment> findByEmployeeId(Long employeeId);
    List<AssetAssignment> findByAssetId(Long assetId);
    boolean existsByEmployeeIdAndStatus(Long employeeId, AssignmentStatus status);
    boolean existsByAssetIdAndStatus(Long assetId, AssignmentStatus status);
}
