package com.peopleops.service;

import com.peopleops.dto.InterviewRequestDTO;
import com.peopleops.dto.InterviewResponseDTO;
import com.peopleops.entity.Candidate;
import com.peopleops.entity.Employee;
import com.peopleops.entity.Interview;
import com.peopleops.enums.CandidateStage;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.enums.InterviewResult;
import com.peopleops.exception.BusinessRuleException;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.CandidateRepository;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.InterviewRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InterviewService {

    private static final Logger log = LoggerFactory.getLogger(InterviewService.class);

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
            log.warn("Interview scheduling failed: candidateId is null");
            throw new BusinessRuleException("Candidate id is required");
        }
        if (request.getInterviewerId() == null) {
            log.warn("Interview scheduling failed: interviewerId is null");
            throw new BusinessRuleException("Interviewer id is required");
        }

        Candidate candidate = candidateRepository.findById(request.getCandidateId())
            .orElseThrow(() -> {
                log.warn("Interview scheduling failed: candidate id {} not found", request.getCandidateId());
                return new ResourceNotFoundException("Candidate not found with id: " + request.getCandidateId());
            });

        if (candidate.getStage() == CandidateStage.APPLIED || candidate.getStage() == CandidateStage.REJECTED || candidate.getStage() == CandidateStage.HIRED) {
            log.warn("Interview scheduling failed: candidate id {} in invalid stage {}", candidate.getId(), candidate.getStage());
            throw new BusinessRuleException("Candidate must be at least in SHORTLISTED stage to schedule an interview");
        }

        Employee interviewer = employeeRepository.findById(request.getInterviewerId())
            .orElseThrow(() -> {
                log.warn("Interview scheduling failed: interviewer id {} not found", request.getInterviewerId());
                return new ResourceNotFoundException("Interviewer not found with id: " + request.getInterviewerId());
            });

        if (interviewer.getStatus() == EmployeeStatus.EXITED) {
            log.warn("Interview scheduling failed: interviewer id {} is EXITED", interviewer.getId());
            throw new BusinessRuleException("Interviewer must be an active or working employee");
        }

        Interview interview = toEntity(request);
        interview.setCandidate(candidate);
        interview.setInterviewer(interviewer);
        interview.setResult(InterviewResult.PENDING);
        Interview saved = interviewRepository.save(interview);
        log.info("Scheduled interview round {} for candidate id: {} with interviewer id: {}", saved.getRoundNumber(), saved.getCandidate().getId(), saved.getInterviewer().getId());
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
            .orElseThrow(() -> {
                log.warn("Interview lookup failed: id {} not found", id);
                return new ResourceNotFoundException("Interview not found with id: " + id);
            });
        return toResponseDTO(interview);
    }

    public InterviewResponseDTO updateInterview(Long id, InterviewRequestDTO request) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Interview update failed: id {} not found", id);
                return new ResourceNotFoundException("Interview not found with id: " + id);
            });

        Candidate candidate = candidateRepository.findById(request.getCandidateId())
            .orElseThrow(() -> {
                log.warn("Interview update failed: candidate id {} not found", request.getCandidateId());
                return new ResourceNotFoundException("Candidate not found with id: " + request.getCandidateId());
            });

        Employee interviewer = employeeRepository.findById(request.getInterviewerId())
            .orElseThrow(() -> {
                log.warn("Interview update failed: interviewer id {} not found", request.getInterviewerId());
                return new ResourceNotFoundException("Interviewer not found with id: " + request.getInterviewerId());
            });

        if (interviewer.getStatus() == EmployeeStatus.EXITED) {
            log.warn("Interview update failed: interviewer id {} is EXITED", interviewer.getId());
            throw new BusinessRuleException("Interviewer must be an active or working employee");
        }

        interview.setCandidate(candidate);
        interview.setInterviewer(interviewer);
        interview.setScheduledAt(request.getScheduledAt());
        interview.setRoundNumber(request.getRoundNumber());
        interview.setFeedback(request.getFeedback());

        Interview updated = interviewRepository.save(interview);
        log.info("Updated interview id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteInterview(Long id) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Interview delete failed: id {} not found", id);
                return new ResourceNotFoundException("Interview not found with id: " + id);
            });
        interviewRepository.delete(interview);
        log.info("Deleted interview id: {}", id);
    }

    public InterviewResponseDTO updateResult(Long id, InterviewResult result, String feedback) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Interview result patch failed: id {} not found", id);
                return new ResourceNotFoundException("Interview not found with id: " + id);
            });

        interview.setResult(result);
        if (feedback != null) {
            interview.setFeedback(feedback);
        }

        if (result == InterviewResult.PASSED) {
            Candidate candidate = interview.getCandidate();
            if (candidate != null && candidate.getStage() == CandidateStage.SHORTLISTED) {
                candidate.setStage(CandidateStage.INTERVIEWED);
                candidateRepository.save(candidate);
                log.info("Advanced candidate id: {} stage to INTERVIEWED after passing interview id: {}", candidate.getId(), id);
            }
        }

        Interview updated = interviewRepository.save(interview);
        log.info("Recorded interview result {} for interview id: {}", result, id);
        return toResponseDTO(updated);
    }

    private Interview toEntity(InterviewRequestDTO dto) {
        Interview interview = new Interview();
        interview.setScheduledAt(dto.getScheduledAt());
        interview.setRoundNumber(dto.getRoundNumber());
        interview.setFeedback(dto.getFeedback());
        return interview;
    }

    private InterviewResponseDTO toResponseDTO(Interview entity) {
        return new InterviewResponseDTO(
            entity.getId(),
            entity.getCandidate() != null ? entity.getCandidate().getId() : null,
            entity.getInterviewer() != null ? entity.getInterviewer().getId() : null,
            entity.getScheduledAt(),
            entity.getRoundNumber(),
            entity.getResult(),
            entity.getFeedback()
        );
    }
}
