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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AttendanceService {

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee id is required");
        }
        if (request.getAttendanceDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attendance date is required");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId()));

        if (employee.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only working employees can record attendance");
        }

        if (request.getAttendanceDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attendance date cannot be in the future");
        }

        if (attendanceRepository.existsByEmployeeIdAndAttendanceDate(request.getEmployeeId(), request.getAttendanceDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance already recorded for this date: " + request.getAttendanceDate());
        }

        if ((request.getStatus() == AttendanceStatus.PRESENT || request.getStatus() == AttendanceStatus.HALF_DAY)
                && request.getCheckIn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in time is required for PRESENT or HALF_DAY status");
        }

        if (request.getCheckIn() != null && request.getCheckOut() != null && request.getCheckOut().isBefore(request.getCheckIn())) {
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance record not found with id: " + id));
        return toResponseDTO(attendance);
    }

    public AttendanceResponseDTO updateAttendance(Long id, AttendanceRequestDTO request) {
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance record not found with id: " + id));

        if (!employeeRepository.existsById(request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId());
        }

        if (request.getAttendanceDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attendance date cannot be in the future");
        }

        if ((!attendance.getEmployeeId().equals(request.getEmployeeId()) || !attendance.getAttendanceDate().equals(request.getAttendanceDate()))
                && attendanceRepository.existsByEmployeeIdAndAttendanceDate(request.getEmployeeId(), request.getAttendanceDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance already recorded for this date: " + request.getAttendanceDate());
        }

        if ((request.getStatus() == AttendanceStatus.PRESENT || request.getStatus() == AttendanceStatus.HALF_DAY)
                && request.getCheckIn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in time is required for PRESENT or HALF_DAY status");
        }

        if (request.getCheckIn() != null && request.getCheckOut() != null && request.getCheckOut().isBefore(request.getCheckIn())) {
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
        return toResponseDTO(updated);
    }

    public void deleteAttendance(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance record not found with id: " + id));
        attendanceRepository.delete(attendance);
    }

    public AttendanceResponseDTO checkOut(Long id, LocalTime checkOutTime) {
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance record not found with id: " + id));

        if (attendance.getCheckIn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot check out without a check-in time");
        }

        if (checkOutTime.isBefore(attendance.getCheckIn())) {
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
