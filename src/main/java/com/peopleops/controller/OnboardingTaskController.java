package com.peopleops.controller;

import com.peopleops.dto.OnboardingTaskRequestDTO;
import com.peopleops.dto.OnboardingTaskResponseDTO;
import com.peopleops.service.OnboardingTaskService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/onboarding-tasks")
public class OnboardingTaskController {

    private final OnboardingTaskService onboardingTaskService;

    public OnboardingTaskController(OnboardingTaskService onboardingTaskService) {
        this.onboardingTaskService = onboardingTaskService;
    }

    @PostMapping
    public ResponseEntity<OnboardingTaskResponseDTO> createTask(@Valid @RequestBody OnboardingTaskRequestDTO request) {
        OnboardingTaskResponseDTO response = onboardingTaskService.createTask(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OnboardingTaskResponseDTO>> getAllTasks(
            @RequestParam(required = false) Long employeeId) {
        List<OnboardingTaskResponseDTO> tasks = onboardingTaskService.getAllTasks(employeeId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnboardingTaskResponseDTO> getTaskById(@PathVariable Long id) {
        OnboardingTaskResponseDTO response = onboardingTaskService.getTaskById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OnboardingTaskResponseDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody OnboardingTaskRequestDTO request) {
        OnboardingTaskResponseDTO response = onboardingTaskService.updateTask(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        onboardingTaskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<OnboardingTaskResponseDTO> completeTask(@PathVariable Long id) {
        OnboardingTaskResponseDTO response = onboardingTaskService.completeTask(id);
        return ResponseEntity.ok(response);
    }
}
