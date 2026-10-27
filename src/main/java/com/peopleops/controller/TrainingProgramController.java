package com.peopleops.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.peopleops.dto.SkillResponseDTO;
import com.peopleops.dto.TrainingProgramRequestDTO;
import com.peopleops.dto.TrainingProgramResponseDTO;
import com.peopleops.enums.TrainingStatus;
import com.peopleops.service.TrainingProgramService;
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

@Tag(name = "Training Programs", description = "Corporate training program management APIs")
@RestController
@RequestMapping("/api/training-programs")
public class TrainingProgramController {

    private final TrainingProgramService trainingProgramService;

    public TrainingProgramController(TrainingProgramService trainingProgramService) {
        this.trainingProgramService = trainingProgramService;
    }

    @PostMapping
    public ResponseEntity<TrainingProgramResponseDTO> createTrainingProgram(
            @Valid @RequestBody TrainingProgramRequestDTO request) {
        TrainingProgramResponseDTO response = trainingProgramService.createTrainingProgram(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TrainingProgramResponseDTO>> getAllTrainingPrograms(
            @RequestParam(required = false) TrainingStatus status) {
        List<TrainingProgramResponseDTO> list = trainingProgramService.getAllTrainingPrograms(status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingProgramResponseDTO> getTrainingProgramById(@PathVariable Long id) {
        TrainingProgramResponseDTO response = trainingProgramService.getTrainingProgramById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingProgramResponseDTO> updateTrainingProgram(
            @PathVariable Long id,
            @Valid @RequestBody TrainingProgramRequestDTO request) {
        TrainingProgramResponseDTO response = trainingProgramService.updateTrainingProgram(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingProgram(@PathVariable Long id) {
        trainingProgramService.deleteTrainingProgram(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TrainingProgramResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam TrainingStatus value) {
        TrainingProgramResponseDTO response = trainingProgramService.updateStatus(id, value);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get skills associated with training program")
    @GetMapping("/{id}/skills")
    public ResponseEntity<List<SkillResponseDTO>> getProgramSkills(@PathVariable Long id) {
        return ResponseEntity.ok(trainingProgramService.getProgramSkills(id));
    }

    @Operation(summary = "Add skill to training program")
    @PostMapping("/{id}/skills/{skillId}")
    public ResponseEntity<List<SkillResponseDTO>> addSkillToProgram(
            @PathVariable Long id,
            @PathVariable Long skillId) {
        return ResponseEntity.ok(trainingProgramService.addSkillToProgram(id, skillId));
    }

    @Operation(summary = "Remove skill from training program")
    @DeleteMapping("/{id}/skills/{skillId}")
    public ResponseEntity<Void> removeSkillFromProgram(
            @PathVariable Long id,
            @PathVariable Long skillId) {
        trainingProgramService.removeSkillFromProgram(id, skillId);
        return ResponseEntity.noContent().build();
    }
}