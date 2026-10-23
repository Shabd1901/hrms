package com.peopleops.controller;

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
}
