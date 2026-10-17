package com.peopleops.service;

import com.peopleops.dto.OnboardingTaskRequestDTO;
import com.peopleops.dto.OnboardingTaskResponseDTO;
import com.peopleops.entity.OnboardingTask;
import com.peopleops.enums.OnboardingTaskStatus;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.OnboardingTaskRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OnboardingTaskService {

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
        if (!employeeRepository.existsById(request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId());
        }

        OnboardingTask task = toEntity(request);
        task.setStatus(OnboardingTaskStatus.PENDING);
        OnboardingTask saved = onboardingTaskRepository.save(task);
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Onboarding task not found with id: " + id));
        return toResponseDTO(task);
    }

    public OnboardingTaskResponseDTO updateTask(Long id, OnboardingTaskRequestDTO request) {
        OnboardingTask task = onboardingTaskRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Onboarding task not found with id: " + id));

        if (!employeeRepository.existsById(request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId());
        }

        task.setEmployeeId(request.getEmployeeId());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        OnboardingTask updated = onboardingTaskRepository.save(task);
        return toResponseDTO(updated);
    }

    public void deleteTask(Long id) {
        OnboardingTask task = onboardingTaskRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Onboarding task not found with id: " + id));
        onboardingTaskRepository.delete(task);
    }

    @Transactional
    public OnboardingTaskResponseDTO completeTask(Long id) {
        OnboardingTask task = onboardingTaskRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Onboarding task not found with id: " + id));

        task.setStatus(OnboardingTaskStatus.DONE);
        task.setCompletedOn(LocalDate.now());
        OnboardingTask updated = onboardingTaskRepository.save(task);

        employeeService.activateIfReady(task.getEmployeeId());

        return toResponseDTO(updated);
    }

    private OnboardingTask toEntity(OnboardingTaskRequestDTO dto) {
        OnboardingTask task = new OnboardingTask();
        task.setEmployeeId(dto.getEmployeeId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        return task;
    }

    private OnboardingTaskResponseDTO toResponseDTO(OnboardingTask entity) {
        return new OnboardingTaskResponseDTO(
            entity.getId(),
            entity.getEmployeeId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getDueDate(),
            entity.getStatus(),
            entity.getCompletedOn()
        );
    }
}
