package com.peopleops.service;

import com.peopleops.dto.TrainingProgramRequestDTO;
import com.peopleops.dto.TrainingProgramResponseDTO;
import com.peopleops.entity.TrainingProgram;
import com.peopleops.enums.TrainingStatus;
import com.peopleops.repository.TrainingProgramRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TrainingProgramService {

    private final TrainingProgramRepository trainingProgramRepository;

    public TrainingProgramService(TrainingProgramRepository trainingProgramRepository) {
        this.trainingProgramRepository = trainingProgramRepository;
    }

    public TrainingProgramResponseDTO createTrainingProgram(TrainingProgramRequestDTO request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate must be on or before endDate");
        }

        TrainingProgram program = toEntity(request);
        program.setStatus(TrainingStatus.PLANNED);
        TrainingProgram saved = trainingProgramRepository.save(program);
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id: " + id));
        return toResponseDTO(program);
    }

    public TrainingProgramResponseDTO updateTrainingProgram(Long id, TrainingProgramRequestDTO request) {
        TrainingProgram program = trainingProgramRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id: " + id));

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate must be on or before endDate");
        }

        program.setTitle(request.getTitle());
        program.setDescription(request.getDescription());
        program.setTrainerName(request.getTrainerName());
        program.setStartDate(request.getStartDate());
        program.setEndDate(request.getEndDate());

        TrainingProgram updated = trainingProgramRepository.save(program);
        return toResponseDTO(updated);
    }

    public void deleteTrainingProgram(Long id) {
        TrainingProgram program = trainingProgramRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id: " + id));
        trainingProgramRepository.delete(program);
    }

    public TrainingProgramResponseDTO updateStatus(Long id, TrainingStatus status) {
        TrainingProgram program = trainingProgramRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id: " + id));

        program.setStatus(status);
        TrainingProgram updated = trainingProgramRepository.save(program);
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
