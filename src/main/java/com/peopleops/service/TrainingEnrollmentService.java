package com.peopleops.service;

import com.peopleops.dto.TrainingEnrollmentRequestDTO;
import com.peopleops.dto.TrainingEnrollmentResponseDTO;
import com.peopleops.entity.Employee;
import com.peopleops.entity.TrainingEnrollment;
import com.peopleops.entity.TrainingProgram;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.enums.EnrollmentStatus;
import com.peopleops.enums.TrainingStatus;
import com.peopleops.exception.BusinessRuleException;
import com.peopleops.exception.DuplicateResourceException;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.TrainingEnrollmentRepository;
import com.peopleops.repository.TrainingProgramRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TrainingEnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(TrainingEnrollmentService.class);

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
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        if (employee.getStatus() == EmployeeStatus.EXITED) {
            log.warn("Enrollment rejected: employee {} is in EXITED status", request.getEmployeeId());
            throw new BusinessRuleException("Only working employees can be enrolled in training");
        }

        TrainingProgram program = trainingProgramRepository.findById(request.getTrainingProgramId())
            .orElseThrow(() -> new ResourceNotFoundException("Training program not found with id: " + request.getTrainingProgramId()));

        if (program.getStatus() != TrainingStatus.PLANNED && program.getStatus() != TrainingStatus.ONGOING) {
            log.warn("Enrollment rejected: program {} has status {}", request.getTrainingProgramId(), program.getStatus());
            throw new BusinessRuleException("Can only enroll into PLANNED or ONGOING training programs");
        }

        if (trainingEnrollmentRepository.existsByTrainingProgramIdAndEmployeeId(request.getTrainingProgramId(), request.getEmployeeId())) {
            log.warn("Enrollment rejected: employee {} is already enrolled in program {}", request.getEmployeeId(), request.getTrainingProgramId());
            throw new DuplicateResourceException("Employee is already enrolled in this training program");
        }

        TrainingEnrollment enrollment = toEntity(request);
        enrollment.setTrainingProgram(program);
        enrollment.setEmployee(employee);
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        TrainingEnrollment saved = trainingEnrollmentRepository.save(enrollment);
        log.info("Enrolled employee {} in training program {} with enrollment id {}", saved.getEmployee() != null ? saved.getEmployee().getId() : null, saved.getTrainingProgram() != null ? saved.getTrainingProgram().getId() : null, saved.getId());
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
            .orElseThrow(() -> new ResourceNotFoundException("Training enrollment not found with id: " + id));
        return toResponseDTO(enrollment);
    }

    public void deleteEnrollment(Long id) {
        TrainingEnrollment enrollment = trainingEnrollmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Training enrollment not found with id: " + id));
        trainingEnrollmentRepository.delete(enrollment);
        log.info("Deleted training enrollment with id {}", id);
    }

    public TrainingEnrollmentResponseDTO completeEnrollment(Long id) {
        TrainingEnrollment enrollment = trainingEnrollmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Training enrollment not found with id: " + id));

        if (enrollment.getStatus() != EnrollmentStatus.ENROLLED) {
            log.warn("Complete enrollment rejected: enrollment {} has status {}", id, enrollment.getStatus());
            throw new BusinessRuleException("Only ENROLLED enrollments can be completed");
        }

        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollment.setCompletionDate(LocalDate.now());

        TrainingEnrollment updated = trainingEnrollmentRepository.save(enrollment);
        log.info("Completed training enrollment id {}", id);
        return toResponseDTO(updated);
    }

    public TrainingEnrollmentResponseDTO dropEnrollment(Long id) {
        TrainingEnrollment enrollment = trainingEnrollmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Training enrollment not found with id: " + id));

        if (enrollment.getStatus() != EnrollmentStatus.ENROLLED) {
            log.warn("Drop enrollment rejected: enrollment {} has status {}", id, enrollment.getStatus());
            throw new BusinessRuleException("Only ENROLLED enrollments can be dropped");
        }

        enrollment.setStatus(EnrollmentStatus.DROPPED);
        TrainingEnrollment updated = trainingEnrollmentRepository.save(enrollment);
        log.info("Dropped training enrollment id {}", id);
        return toResponseDTO(updated);
    }

    private TrainingEnrollment toEntity(TrainingEnrollmentRequestDTO dto) {
        return new TrainingEnrollment();
    }

    private TrainingEnrollmentResponseDTO toResponseDTO(TrainingEnrollment entity) {
        return new TrainingEnrollmentResponseDTO(
            entity.getId(),
            entity.getTrainingProgram() != null ? entity.getTrainingProgram().getId() : null,
            entity.getEmployee() != null ? entity.getEmployee().getId() : null,
            entity.getStatus(),
            entity.getCompletionDate()
        );
    }
}
