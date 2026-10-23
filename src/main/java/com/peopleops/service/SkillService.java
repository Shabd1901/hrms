package com.peopleops.service;

import com.peopleops.dto.SkillRequestDTO;
import com.peopleops.dto.SkillResponseDTO;
import com.peopleops.entity.Skill;
import com.peopleops.exception.DuplicateResourceException;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.SkillRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SkillService {

    private static final Logger log = LoggerFactory.getLogger(SkillService.class);

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public SkillResponseDTO createSkill(SkillRequestDTO request) {
        if (skillRepository.existsByName(request.getName())) {
            log.warn("Skill creation failed: duplicate name '{}'", request.getName());
            throw new DuplicateResourceException("Skill already exists with name: " + request.getName());
        }

        Skill skill = toEntity(request);
        Skill saved = skillRepository.save(skill);
        log.info("Created skill with id: {} and name: '{}'", saved.getId(), saved.getName());
        return toResponseDTO(saved);
    }

    public List<SkillResponseDTO> getAllSkills() {
        List<Skill> list = skillRepository.findAll();
        List<SkillResponseDTO> responses = new ArrayList<>();
        for (Skill s : list) {
            responses.add(toResponseDTO(s));
        }
        return responses;
    }

    public SkillResponseDTO getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Skill lookup failed: id {} not found", id);
                return new ResourceNotFoundException("Skill not found with id: " + id);
            });
        return toResponseDTO(skill);
    }

    public SkillResponseDTO updateSkill(Long id, SkillRequestDTO request) {
        Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Skill update failed: id {} not found", id);
                return new ResourceNotFoundException("Skill not found with id: " + id);
            });

        if (!skill.getName().equals(request.getName()) && skillRepository.existsByName(request.getName())) {
            log.warn("Skill update failed for id {}: duplicate name '{}'", id, request.getName());
            throw new DuplicateResourceException("Skill already exists with name: " + request.getName());
        }

        skill.setName(request.getName());
        skill.setCategory(request.getCategory());

        Skill updated = skillRepository.save(skill);
        log.info("Updated skill id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteSkill(Long id) {
        Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Skill delete failed: id {} not found", id);
                return new ResourceNotFoundException("Skill not found with id: " + id);
            });
        skillRepository.delete(skill);
        log.info("Deleted skill id: {}", id);
    }

    private Skill toEntity(SkillRequestDTO dto) {
        Skill s = new Skill();
        s.setName(dto.getName());
        s.setCategory(dto.getCategory());
        return s;
    }

    private SkillResponseDTO toResponseDTO(Skill entity) {
        return new SkillResponseDTO(
            entity.getId(),
            entity.getName(),
            entity.getCategory()
        );
    }
}
