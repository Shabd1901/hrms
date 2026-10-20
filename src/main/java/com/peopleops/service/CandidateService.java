package com.peopleops.service;

import com.peopleops.dto.CandidateRequestDTO;
import com.peopleops.dto.CandidateResponseDTO;
import com.peopleops.dto.EmployeeResponseDTO;
import com.peopleops.dto.HireRequestDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final JobOpeningRepository jobOpeningRepository;
    private final EmployeeService employeeService;

    public CandidateService(
            CandidateRepository candidateRepository,
            JobOpeningRepository jobOpeningRepository,
            EmployeeService employeeService) {
        this.candidateRepository = candidateRepository;
        this.jobOpeningRepository = jobOpeningRepository;
        this.employeeService = employeeService;
    }

    public CandidateResponseDTO createCandidate(CandidateRequestDTO request) {
        if (request.getJobOpeningId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job opening id is required");
        }
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

    @Transactional
    public EmployeeResponseDTO hireCandidate(Long id, HireRequestDTO request) {
        Candidate candidate = candidateRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found with id: " + id));

        if (candidate.getStage() != CandidateStage.OFFERED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate can only be hired when in OFFERED stage");
        }

        JobOpening opening = jobOpeningRepository.findById(candidate.getJobOpeningId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found with id: " + candidate.getJobOpeningId()));

        if (opening.getStatus() != JobOpeningStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot hire for a job opening that is not OPEN");
        }

        candidate.setStage(CandidateStage.HIRED);
        candidateRepository.save(candidate);

        int remaining = opening.getOpenPositions() - 1;
        opening.setOpenPositions(remaining);
        if (remaining <= 0) {
            opening.setStatus(JobOpeningStatus.CLOSED);
        }
        jobOpeningRepository.save(opening);

        return employeeService.createFromCandidate(candidate, opening, request);
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
