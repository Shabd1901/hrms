package com.peopleops.service;

import com.peopleops.dto.TrainingEnrollmentRequestDTO;
import com.peopleops.dto.TrainingEnrollmentResponseDTO;
import com.peopleops.entity.Employee;
import com.peopleops.entity.TrainingEnrollment;
import com.peopleops.entity.TrainingProgram;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.enums.EnrollmentStatus;
import com.peopleops.enums.TrainingStatus;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.TrainingEnrollmentRepository;
import com.peopleops.repository.TrainingProgramRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TrainingEnrollmentService {

    private final TrainingEnrollmentRepository trainingEnrollmentRepository;
    private final TrainingProgramRepository trainingProgramRepository;
    private final EmployeeRepository employeeRepository;

    public TrainingEnrollmentService(
            TrainingEnrollmentRepository trainingEnrollmentRepository,
            TrainingProgramRepository trainingProgramRepository,
            EmployeeRepository employeeRepository) {
        this.trainingEnrollmentRepository = trainingEnrollmentRepository;
        this.trainingProgramRepository = trainingProgramRepository;
        this.employeeRepository = employeeRepository;
    }

    public TrainingEnrollmentResponseDTO enrollEmployee(TrainingEnrollmentRequestDTO request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId()));

        if (employee.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only working employees can be enrolled in training");
        }

        TrainingProgram program = trainingProgramRepository.findById(request.getTrainingProgramId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id: " + request.getTrainingProgramId()));

        if (program.getStatus() != TrainingStatus.PLANNED && program.getStatus() != TrainingStatus.ONGOING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can only enroll into PLANNED or ONGOING training programs");
        }

        if (trainingEnrollmentRepository.existsByTrainingProgramIdAndEmployeeId(request.getTrainingProgramId(), request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee is already enrolled in this training program");
        }

        TrainingEnrollment enrollment = toEntity(request);
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        TrainingEnrollment saved = trainingEnrollmentRepository.save(enrollment);
        return toResponseDTO(saved);
    }

    public List<TrainingEnrollmentResponseDTO> getAllEnrollments(Long employeeId) {
        List<TrainingEnrollment> list = (employeeId != null)
            ? trainingEnrollmentRepository.findByEmployeeId(employeeId)
            : trainingEnrollmentRepository.findAll();

        List<TrainingEnrollmentResponseDTO> responses = new ArrayList<>();
        for (TrainingEnrollment te : list) {
            responses.add(toResponseDTO(te));
        }
        return responses;
    }

    public TrainingEnrollmentResponseDTO getEnrollmentById(Long id) {
        TrainingEnrollment enrollment = trainingEnrollmentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training enrollment not found with id: " + id));
        return toResponseDTO(enrollment);
    }

    public void deleteEnrollment(Long id) {
        TrainingEnrollment enrollment = trainingEnrollmentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training enrollment not found with id: " + id));
        trainingEnrollmentRepository.delete(enrollment);
    }

    public TrainingEnrollmentResponseDTO completeEnrollment(Long id) {
        TrainingEnrollment enrollment = trainingEnrollmentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training enrollment not found with id: " + id));

        if (enrollment.getStatus() != EnrollmentStatus.ENROLLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only ENROLLED enrollments can be completed");
        }

        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollment.setCompletionDate(LocalDate.now());

        TrainingEnrollment updated = trainingEnrollmentRepository.save(enrollment);
        return toResponseDTO(updated);
    }

    public TrainingEnrollmentResponseDTO dropEnrollment(Long id) {
        TrainingEnrollment enrollment = trainingEnrollmentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training enrollment not found with id: " + id));

        if (enrollment.getStatus() != EnrollmentStatus.ENROLLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only ENROLLED enrollments can be dropped");
        }

        enrollment.setStatus(EnrollmentStatus.DROPPED);
        TrainingEnrollment updated = trainingEnrollmentRepository.save(enrollment);
        return toResponseDTO(updated);
    }

    private TrainingEnrollment toEntity(TrainingEnrollmentRequestDTO dto) {
        TrainingEnrollment te = new TrainingEnrollment();
        te.setTrainingProgramId(dto.getTrainingProgramId());
        te.setEmployeeId(dto.getEmployeeId());
        return te;
    }

    private TrainingEnrollmentResponseDTO toResponseDTO(TrainingEnrollment entity) {
        return new TrainingEnrollmentResponseDTO(
            entity.getId(),
            entity.getTrainingProgramId(),
            entity.getEmployeeId(),
            entity.getStatus(),
            entity.getCompletionDate()
        );
    }
}
