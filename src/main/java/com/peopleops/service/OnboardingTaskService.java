package com.peopleops.service;

import com.peopleops.dto.OnboardingTaskRequestDTO;
import com.peopleops.dto.OnboardingTaskResponseDTO;
import com.peopleops.entity.Employee;
import com.peopleops.entity.OnboardingTask;
import com.peopleops.enums.OnboardingTaskStatus;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.OnboardingTaskRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OnboardingTaskService {

    private static final Logger log = LoggerFactory.getLogger(OnboardingTaskService.class);

    private final OnboardingTaskRepository onboardingTaskRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    public OnboardingTaskService(
            OnboardingTaskRepository onboardingTaskRepository,
            EmployeeRepository employeeRepository,
            EmployeeService employeeService) {
        this.onboardingTaskRepository = onboardingTaskRepository;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    public OnboardingTaskResponseDTO createTask(OnboardingTaskRequestDTO request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> {
                log.warn("Onboarding task creation failed: employee id {} not found", request.getEmployeeId());
                return new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId());
            });

        OnboardingTask task = toEntity(request);
        task.setEmployee(employee);
        task.setStatus(OnboardingTaskStatus.PENDING);
        OnboardingTask saved = onboardingTaskRepository.save(task);
        log.info("Created onboarding task id: {} for employee id: {}", saved.getId(), saved.getEmployee().getId());
        return toResponseDTO(saved);
    }

    public List<OnboardingTaskResponseDTO> getAllTasks(Long employeeId) {
        List<OnboardingTask> tasks = (employeeId != null)
            ? onboardingTaskRepository.findByEmployeeId(employeeId)
            : onboardingTaskRepository.findAll();

        List<OnboardingTaskResponseDTO> responses = new ArrayList<>();
        for (OnboardingTask task : tasks) {
            responses.add(toResponseDTO(task));
        }
        return responses;
    }

    public OnboardingTaskResponseDTO getTaskById(Long id) {
        OnboardingTask task = onboardingTaskRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Onboarding task lookup failed: id {} not found", id);
                return new ResourceNotFoundException("Onboarding task not found with id: " + id);
            });
        return toResponseDTO(task);
    }

    public OnboardingTaskResponseDTO updateTask(Long id, OnboardingTaskRequestDTO request) {
        OnboardingTask task = onboardingTaskRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Onboarding task update failed: id {} not found", id);
                return new ResourceNotFoundException("Onboarding task not found with id: " + id);
            });

        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> {
                log.warn("Onboarding task update failed for id {}: employee id {} not found", id, request.getEmployeeId());
                return new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId());
            });

        task.setEmployee(employee);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        OnboardingTask updated = onboardingTaskRepository.save(task);
        log.info("Updated onboarding task id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteTask(Long id) {
        OnboardingTask task = onboardingTaskRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Onboarding task delete failed: id {} not found", id);
                return new ResourceNotFoundException("Onboarding task not found with id: " + id);
            });
        onboardingTaskRepository.delete(task);
        log.info("Deleted onboarding task id: {}", id);
    }

    @Transactional
    public OnboardingTaskResponseDTO completeTask(Long id) {
        OnboardingTask task = onboardingTaskRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Onboarding task complete failed: id {} not found", id);
                return new ResourceNotFoundException("Onboarding task not found with id: " + id);
            });

        task.setStatus(OnboardingTaskStatus.DONE);
        task.setCompletedOn(LocalDate.now());
        OnboardingTask updated = onboardingTaskRepository.save(task);

        Long employeeId = task.getEmployee() != null ? task.getEmployee().getId() : null;
        log.info("Completed onboarding task id: {} for employee id: {}", id, employeeId);

        if (employeeId != null) {
            employeeService.activateIfReady(employeeId);
        }

        return toResponseDTO(updated);
    }

    private OnboardingTask toEntity(OnboardingTaskRequestDTO dto) {
        OnboardingTask task = new OnboardingTask();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        return task;
    }

    private OnboardingTaskResponseDTO toResponseDTO(OnboardingTask entity) {
        return new OnboardingTaskResponseDTO(
            entity.getId(),
            entity.getEmployee() != null ? entity.getEmployee().getId() : null,
            entity.getTitle(),
            entity.getDescription(),
            entity.getDueDate(),
            entity.getStatus(),
            entity.getCompletedOn()
        );
    }
}
