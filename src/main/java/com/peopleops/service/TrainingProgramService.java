package com.peopleops.service;

import com.peopleops.dto.TrainingProgramRequestDTO;
import com.peopleops.dto.TrainingProgramResponseDTO;
import com.peopleops.entity.TrainingProgram;
import com.peopleops.enums.TrainingStatus;
import com.peopleops.repository.TrainingProgramRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TrainingProgramService {

    private static final Logger log = LoggerFactory.getLogger(TrainingProgramService.class);

    private final TrainingProgramRepository trainingProgramRepository;

    public TrainingProgramService(TrainingProgramRepository trainingProgramRepository) {
        this.trainingProgramRepository = trainingProgramRepository;
    }

    public TrainingProgramResponseDTO createTrainingProgram(TrainingProgramRequestDTO request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            log.warn("Training program creation failed: startDate {} is after endDate {}", request.getStartDate(), request.getEndDate());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate must be on or before endDate");
        }

        TrainingProgram program = toEntity(request);
        program.setStatus(TrainingStatus.PLANNED);
        TrainingProgram saved = trainingProgramRepository.save(program);
        log.info("Created training program with id: {} and title: '{}'", saved.getId(), saved.getTitle());
        return toResponseDTO(saved);
    }

    public List<TrainingProgramResponseDTO> getAllTrainingPrograms(TrainingStatus status) {
        List<TrainingProgram> list = (status != null)
            ? trainingProgramRepository.findByStatus(status)
            : trainingProgramRepository.findAll();

        List<TrainingProgramResponseDTO> responses = new ArrayList<>();
        for (TrainingProgram tp : list) {
            responses.add(toResponseDTO(tp));
        }
        return responses;
    }

    public TrainingProgramResponseDTO getTrainingProgramById(Long id) {
        TrainingProgram program = trainingProgramRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Training program lookup failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id: " + id);
            });
        return toResponseDTO(program);
    }

    public TrainingProgramResponseDTO updateTrainingProgram(Long id, TrainingProgramRequestDTO request) {
        TrainingProgram program = trainingProgramRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Training program update failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id: " + id);
            });

        if (request.getStartDate().isAfter(request.getEndDate())) {
            log.warn("Training program update failed for id {}: startDate {} is after endDate {}", id, request.getStartDate(), request.getEndDate());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate must be on or before endDate");
        }

        program.setTitle(request.getTitle());
        program.setDescription(request.getDescription());
        program.setTrainerName(request.getTrainerName());
        program.setStartDate(request.getStartDate());
        program.setEndDate(request.getEndDate());

        TrainingProgram updated = trainingProgramRepository.save(program);
        log.info("Updated training program id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteTrainingProgram(Long id) {
        TrainingProgram program = trainingProgramRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Training program delete failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id: " + id);
            });
        trainingProgramRepository.delete(program);
        log.info("Deleted training program id: {}", id);
    }

    public TrainingProgramResponseDTO updateStatus(Long id, TrainingStatus status) {
        TrainingProgram program = trainingProgramRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Training program status update failed: id {} not found", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id: " + id);
            });

        program.setStatus(status);
        TrainingProgram updated = trainingProgramRepository.save(program);
        log.info("Updated training program id: {} status to {}", id, status);
        return toResponseDTO(updated);
    }

    private TrainingProgram toEntity(TrainingProgramRequestDTO dto) {
        TrainingProgram tp = new TrainingProgram();
        tp.setTitle(dto.getTitle());
        tp.setDescription(dto.getDescription());
        tp.setTrainerName(dto.getTrainerName());
        tp.setStartDate(dto.getStartDate());
        tp.setEndDate(dto.getEndDate());
        return tp;
    }

    private TrainingProgramResponseDTO toResponseDTO(TrainingProgram entity) {
        return new TrainingProgramResponseDTO(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getTrainerName(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getStatus()
        );
    }
}
