package com.peopleops.repository;

import com.peopleops.entity.LeaveBalance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByEmployeeIdAndLeaveTypeIdAndBalanceYear(Long employeeId, Long leaveTypeId, Integer balanceYear);
    boolean existsByEmployeeIdAndLeaveTypeIdAndBalanceYear(Long employeeId, Long leaveTypeId, Integer balanceYear);
    List<LeaveBalance> findByEmployeeIdAndBalanceYear(Long employeeId, Integer balanceYear);
    List<LeaveBalance> findByEmployeeId(Long employeeId);
    List<LeaveBalance> findByBalanceYear(Integer balanceYear);
}
