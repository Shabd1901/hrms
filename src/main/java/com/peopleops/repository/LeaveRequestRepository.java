package com.peopleops.repository;

import com.peopleops.entity.LeaveRequest;
import com.peopleops.enums.LeaveRequestStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeId(Long employeeId);
    List<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, LeaveRequestStatus status);
    List<LeaveRequest> findByStatus(LeaveRequestStatus status);
}
