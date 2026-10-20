package com.peopleops.service;

import com.peopleops.dto.InterviewRequestDTO;
import com.peopleops.dto.InterviewResponseDTO;
import com.peopleops.entity.Candidate;
import com.peopleops.entity.Employee;
import com.peopleops.entity.Interview;
import com.peopleops.enums.CandidateStage;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.enums.InterviewResult;
import com.peopleops.repository.CandidateRepository;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.InterviewRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final CandidateRepository candidateRepository;
    private final EmployeeRepository employeeRepository;

    public InterviewService(
            InterviewRepository interviewRepository,
            CandidateRepository candidateRepository,
            EmployeeRepository employeeRepository) {
        this.interviewRepository = interviewRepository;
        this.candidateRepository = candidateRepository;
        this.employeeRepository = employeeRepository;
    }

    public InterviewResponseDTO createInterview(InterviewRequestDTO request) {
        if (request.getCandidateId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate id is required");
        }
        if (request.getInterviewerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Interviewer id is required");
        }

        Candidate candidate = candidateRepository.findById(request.getCandidateId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found with id: " + request.getCandidateId()));

        if (candidate.getStage() == CandidateStage.APPLIED || candidate.getStage() == CandidateStage.REJECTED || candidate.getStage() == CandidateStage.HIRED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate must be at least in SHORTLISTED stage to schedule an interview");
        }

        Employee interviewer = employeeRepository.findById(request.getInterviewerId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interviewer not found with id: " + request.getInterviewerId()));

        if (interviewer.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Interviewer must be an active or working employee");
        }

        Interview interview = toEntity(request);
        interview.setResult(InterviewResult.PENDING);
        Interview saved = interviewRepository.save(interview);
        return toResponseDTO(saved);
    }

    public List<InterviewResponseDTO> getAllInterviews(Long candidateId) {
        List<Interview> interviews = (candidateId != null)
            ? interviewRepository.findByCandidateId(candidateId)
            : interviewRepository.findAll();

        List<InterviewResponseDTO> responses = new ArrayList<>();
        for (Interview interview : interviews) {
            responses.add(toResponseDTO(interview));
        }
        return responses;
    }

    public InterviewResponseDTO getInterviewById(Long id) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interview not found with id: " + id));
        return toResponseDTO(interview);
    }

    public InterviewResponseDTO updateInterview(Long id, InterviewRequestDTO request) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interview not found with id: " + id));

        if (!candidateRepository.existsById(request.getCandidateId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found with id: " + request.getCandidateId());
        }

        Employee interviewer = employeeRepository.findById(request.getInterviewerId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interviewer not found with id: " + request.getInterviewerId()));

        if (interviewer.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Interviewer must be an active or working employee");
        }

        interview.setCandidateId(request.getCandidateId());
        interview.setInterviewerId(request.getInterviewerId());
        interview.setScheduledAt(request.getScheduledAt());
        interview.setRoundNumber(request.getRoundNumber());
        interview.setFeedback(request.getFeedback());

        Interview updated = interviewRepository.save(interview);
        return toResponseDTO(updated);
    }

    public void deleteInterview(Long id) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interview not found with id: " + id));
        interviewRepository.delete(interview);
    }

    public InterviewResponseDTO updateResult(Long id, InterviewResult result, String feedback) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interview not found with id: " + id));

        interview.setResult(result);
        if (feedback != null) {
            interview.setFeedback(feedback);
        }

        if (result == InterviewResult.PASSED) {
            Candidate candidate = candidateRepository.findById(interview.getCandidateId()).orElse(null);
            if (candidate != null && candidate.getStage() == CandidateStage.SHORTLISTED) {
                candidate.setStage(CandidateStage.INTERVIEWED);
                candidateRepository.save(candidate);
            }
        }

        Interview updated = interviewRepository.save(interview);
        return toResponseDTO(updated);
    }

    private Interview toEntity(InterviewRequestDTO dto) {
        Interview interview = new Interview();
        interview.setCandidateId(dto.getCandidateId());
        interview.setInterviewerId(dto.getInterviewerId());
        interview.setScheduledAt(dto.getScheduledAt());
        interview.setRoundNumber(dto.getRoundNumber());
        interview.setFeedback(dto.getFeedback());
        return interview;
    }

    private InterviewResponseDTO toResponseDTO(Interview entity) {
        return new InterviewResponseDTO(
            entity.getId(),
            entity.getCandidateId(),
            entity.getInterviewerId(),
            entity.getScheduledAt(),
            entity.getRoundNumber(),
            entity.getResult(),
            entity.getFeedback()
        );
    }
}
