package com.peopleops.service;

import com.peopleops.dto.CandidateRequestDTO;
import com.peopleops.dto.CandidateResponseDTO;
import com.peopleops.entity.Candidate;
import com.peopleops.entity.JobOpening;
import com.peopleops.enums.CandidateStage;
import com.peopleops.enums.JobOpeningStatus;
import com.peopleops.repository.CandidateRepository;
import com.peopleops.repository.JobOpeningRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final JobOpeningRepository jobOpeningRepository;

    public CandidateService(
            CandidateRepository candidateRepository,
            JobOpeningRepository jobOpeningRepository) {
        this.candidateRepository = candidateRepository;
        this.jobOpeningRepository = jobOpeningRepository;
    }

    public CandidateResponseDTO createCandidate(CandidateRequestDTO request) {
        JobOpening opening = jobOpeningRepository.findById(request.getJobOpeningId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found with id: " + request.getJobOpeningId()));

        if (opening.getStatus() != JobOpeningStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot apply to a job opening that is not OPEN");
        }

        if (candidateRepository.existsByJobOpeningIdAndEmail(request.getJobOpeningId(), request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Candidate has already applied for this opening with email: " + request.getEmail());
        }

        Candidate candidate = toEntity(request);
        candidate.setStage(CandidateStage.APPLIED);
        Candidate saved = candidateRepository.save(candidate);
        return toResponseDTO(saved);
    }

    public List<CandidateResponseDTO> getAllCandidates(Long jobOpeningId) {
        List<Candidate> candidates = (jobOpeningId != null)
            ? candidateRepository.findByJobOpeningId(jobOpeningId)
            : candidateRepository.findAll();

        List<CandidateResponseDTO> responses = new ArrayList<>();
        for (Candidate c : candidates) {
            responses.add(toResponseDTO(c));
        }
        return responses;
    }

    public CandidateResponseDTO getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found with id: " + id));
        return toResponseDTO(candidate);
    }

    public CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO request) {
        Candidate candidate = candidateRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found with id: " + id));

        if (!jobOpeningRepository.existsById(request.getJobOpeningId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found with id: " + request.getJobOpeningId());
        }

        if ((!candidate.getJobOpeningId().equals(request.getJobOpeningId()) || !candidate.getEmail().equals(request.getEmail()))
                && candidateRepository.existsByJobOpeningIdAndEmail(request.getJobOpeningId(), request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Candidate has already applied for this opening with email: " + request.getEmail());
        }

        candidate.setJobOpeningId(request.getJobOpeningId());
        candidate.setFirstName(request.getFirstName());
        candidate.setLastName(request.getLastName());
        candidate.setEmail(request.getEmail());
        candidate.setPhone(request.getPhone());
        candidate.setResumeNotes(request.getResumeNotes());
        candidate.setAppliedDate(request.getAppliedDate());

        Candidate updated = candidateRepository.save(candidate);
        return toResponseDTO(updated);
    }

    public void deleteCandidate(Long id) {
        Candidate candidate = candidateRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found with id: " + id));
        candidateRepository.delete(candidate);
    }

    public CandidateResponseDTO updateStage(Long id, CandidateStage targetStage) {
        Candidate candidate = candidateRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found with id: " + id));

        CandidateStage currentStage = candidate.getStage();
        if (currentStage == CandidateStage.HIRED || currentStage == CandidateStage.REJECTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot change stage of a candidate who is already " + currentStage);
        }

        if (targetStage == CandidateStage.HIRED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "HIRED stage can only be set via the hire endpoint");
        }

        if (targetStage != CandidateStage.REJECTED) {
            boolean validForwardMove =
                (currentStage == CandidateStage.APPLIED && targetStage == CandidateStage.SHORTLISTED) ||
                (currentStage == CandidateStage.SHORTLISTED && targetStage == CandidateStage.INTERVIEWED) ||
                (currentStage == CandidateStage.INTERVIEWED && targetStage == CandidateStage.OFFERED);

            if (!validForwardMove) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid stage transition from " + currentStage + " to " + targetStage);
            }
        }

        candidate.setStage(targetStage);
        Candidate updated = candidateRepository.save(candidate);
        return toResponseDTO(updated);
    }

    private Candidate toEntity(CandidateRequestDTO dto) {
        Candidate candidate = new Candidate();
        candidate.setJobOpeningId(dto.getJobOpeningId());
        candidate.setFirstName(dto.getFirstName());
        candidate.setLastName(dto.getLastName());
        candidate.setEmail(dto.getEmail());
        candidate.setPhone(dto.getPhone());
        candidate.setResumeNotes(dto.getResumeNotes());
        candidate.setAppliedDate(dto.getAppliedDate());
        return candidate;
    }

    private CandidateResponseDTO toResponseDTO(Candidate entity) {
        return new CandidateResponseDTO(
            entity.getId(),
            entity.getJobOpeningId(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getResumeNotes(),
            entity.getAppliedDate(),
            entity.getStage()
        );
    }
}
