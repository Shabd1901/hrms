package com.peopleops.repository;

import com.peopleops.entity.Attendance;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);
    boolean existsByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);
    List<Attendance> findByEmployeeId(Long employeeId);
    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);
    List<Attendance> findByEmployeeIdAndAttendanceDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}
