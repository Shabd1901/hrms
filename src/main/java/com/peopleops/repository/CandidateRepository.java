package com.peopleops.repository;

import com.peopleops.entity.Candidate;
import com.peopleops.enums.CandidateStage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    boolean existsByJobOpeningIdAndEmail(Long jobOpeningId, String email);
    List<Candidate> findByJobOpeningId(Long jobOpeningId);
    List<Candidate> findByStage(CandidateStage stage);
    boolean existsByJobOpeningId(Long jobOpeningId);
}
