package com.peopleops.service;

import com.peopleops.dto.AttendanceRequestDTO;
import com.peopleops.dto.AttendanceResponseDTO;
import com.peopleops.entity.Attendance;
import com.peopleops.entity.Employee;
import com.peopleops.enums.AttendanceStatus;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.repository.AttendanceRepository;
import com.peopleops.repository.EmployeeRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AttendanceService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    public AttendanceResponseDTO createAttendance(AttendanceRequestDTO request) {
        if (request.getEmployeeId() == null) {
            log.warn("Attendance creation failed: employeeId is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee id is required");
        }
        if (request.getAttendanceDate() == null) {
            log.warn("Attendance creation failed: attendanceDate is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attendance date is required");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> {
                log.warn("Attendance creation failed: employee id {} not found", request.getEmployeeId());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId());
            });

        if (employee.getStatus() == EmployeeStatus.EXITED) {
            log.warn("Attendance creation failed: employee id {} is EXITED", employee.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only working employees can record attendance");
        }

        if (request.getAttendanceDate().isAfter(LocalDate.now())) {
            log.warn("Attendance creation failed: future attendance date {}", request.getAttendanceDate());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attendance date cannot be in the future");
        }

        if (attendanceRepository.existsByEmployeeIdAndAttendanceDate(request.getEmployeeId(), request.getAttendanceDate())) {
            log.warn("Attendance creation failed: duplicate record for employee id {} on {}", request.getEmployeeId(), request.getAttendanceDate());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance already recorded for this date: " + request.getAttendanceDate());
        }

        if ((request.getStatus() == AttendanceStatus.PRESENT || request.getStatus() == AttendanceStatus.HALF_DAY)
                && request.getCheckIn() == null) {
            log.warn("Attendance creation failed: check-in time missing for status {}", request.getStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in time is required for PRESENT or HALF_DAY status");
        }

        if (request.getCheckIn() != null && request.getCheckOut() != null && request.getCheckOut().isBefore(request.getCheckIn())) {
            log.warn("Attendance creation failed: check-out {} is before check-in {}", request.getCheckOut(), request.getCheckIn());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out time must be after check-in time");
        }

        AttendanceStatus finalStatus = request.getStatus();
        if (request.getCheckIn() != null && request.getCheckOut() != null) {
            long minutes = Duration.between(request.getCheckIn(), request.getCheckOut()).toMinutes();
            if (minutes < 240 && finalStatus == AttendanceStatus.PRESENT) {
                finalStatus = AttendanceStatus.HALF_DAY;
            }
        }

        Attendance attendance = toEntity(request);
        attendance.setStatus(finalStatus);
        Attendance saved = attendanceRepository.save(attendance);
        log.info("Recorded attendance id: {} for employee id: {} on date: {} with status: {}", saved.getId(), saved.getEmployeeId(), saved.getAttendanceDate(), saved.getStatus());
        return toResponseDTO(saved);
    }

    public List<AttendanceResponseDTO> getAllAttendance(Long employeeId, LocalDate date) {
        List<Attendance> list = new ArrayList<>();
        if (employeeId != null && date != null) {
            attendanceRepository.findByEmployeeIdAndAttendanceDate(employeeId, date).ifPresent(list::add);
        } else if (employeeId != null) {
            list = attendanceRepository.findByEmployeeId(employeeId);
        } else if (date != null) {
            list = attendanceRepository.findByAttendanceDate(date);
        } else {
            list = attendanceRepository.findAll();
        }

        List<AttendanceResponseDTO> responses = new ArrayList<>();
        for (Attendance a : list) {
            responses.add(toResponseDTO(a));
        }
        return responses;
    }

    public AttendanceResponseDTO getAttendanceById(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Attendance lookup failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance record not found with id: " + id);
            });
        return toResponseDTO(attendance);
    }

    public AttendanceResponseDTO updateAttendance(Long id, AttendanceRequestDTO request) {
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Attendance update failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance record not found with id: " + id);
            });

        if (!employeeRepository.existsById(request.getEmployeeId())) {
            log.warn("Attendance update failed for id {}: employee id {} not found", id, request.getEmployeeId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId());
        }

        if (request.getAttendanceDate().isAfter(LocalDate.now())) {
            log.warn("Attendance update failed for id {}: future date {}", id, request.getAttendanceDate());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attendance date cannot be in the future");
        }

        if ((!attendance.getEmployeeId().equals(request.getEmployeeId()) || !attendance.getAttendanceDate().equals(request.getAttendanceDate()))
                && attendanceRepository.existsByEmployeeIdAndAttendanceDate(request.getEmployeeId(), request.getAttendanceDate())) {
            log.warn("Attendance update failed for id {}: duplicate record for employee id {} on {}", id, request.getEmployeeId(), request.getAttendanceDate());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance already recorded for this date: " + request.getAttendanceDate());
        }

        if ((request.getStatus() == AttendanceStatus.PRESENT || request.getStatus() == AttendanceStatus.HALF_DAY)
                && request.getCheckIn() == null) {
            log.warn("Attendance update failed for id {}: check-in time missing for status {}", id, request.getStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in time is required for PRESENT or HALF_DAY status");
        }

        if (request.getCheckIn() != null && request.getCheckOut() != null && request.getCheckOut().isBefore(request.getCheckIn())) {
            log.warn("Attendance update failed for id {}: check-out {} is before check-in {}", id, request.getCheckOut(), request.getCheckIn());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out time must be after check-in time");
        }

        AttendanceStatus finalStatus = request.getStatus();
        if (request.getCheckIn() != null && request.getCheckOut() != null) {
            long minutes = Duration.between(request.getCheckIn(), request.getCheckOut()).toMinutes();
            if (minutes < 240 && finalStatus == AttendanceStatus.PRESENT) {
                finalStatus = AttendanceStatus.HALF_DAY;
            }
        }

        attendance.setEmployeeId(request.getEmployeeId());
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setCheckIn(request.getCheckIn());
        attendance.setCheckOut(request.getCheckOut());
        attendance.setStatus(finalStatus);

        Attendance updated = attendanceRepository.save(attendance);
        log.info("Updated attendance id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteAttendance(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Attendance delete failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance record not found with id: " + id);
            });
        attendanceRepository.delete(attendance);
        log.info("Deleted attendance id: {}", id);
    }

    public AttendanceResponseDTO checkOut(Long id, LocalTime checkOutTime) {
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Attendance check-out failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance record not found with id: " + id);
            });

        if (attendance.getCheckIn() == null) {
            log.warn("Attendance check-out failed for id {}: checkIn is null", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot check out without a check-in time");
        }

        if (checkOutTime.isBefore(attendance.getCheckIn())) {
            log.warn("Attendance check-out failed for id {}: checkOutTime {} before checkIn {}", id, checkOutTime, attendance.getCheckIn());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out time must be after check-in time");
        }

        attendance.setCheckOut(checkOutTime);
        long minutes = Duration.between(attendance.getCheckIn(), checkOutTime).toMinutes();
        if (minutes < 240) {
            attendance.setStatus(AttendanceStatus.HALF_DAY);
        } else {
            attendance.setStatus(AttendanceStatus.PRESENT);
        }

        Attendance updated = attendanceRepository.save(attendance);
        log.info("Checked out attendance id: {} at time: {} with derived status: {}", id, checkOutTime, updated.getStatus());
        return toResponseDTO(updated);
    }

    private Attendance toEntity(AttendanceRequestDTO dto) {
        Attendance a = new Attendance();
        a.setEmployeeId(dto.getEmployeeId());
        a.setAttendanceDate(dto.getAttendanceDate());
        a.setCheckIn(dto.getCheckIn());
        a.setCheckOut(dto.getCheckOut());
        a.setStatus(dto.getStatus());
        return a;
    }

    private AttendanceResponseDTO toResponseDTO(Attendance entity) {
        return new AttendanceResponseDTO(
            entity.getId(),
            entity.getEmployeeId(),
            entity.getAttendanceDate(),
            entity.getCheckIn(),
            entity.getCheckOut(),
            entity.getStatus()
        );
    }
}
