package com.peopleops.service;

import com.peopleops.dto.SkillRequestDTO;
import com.peopleops.dto.SkillResponseDTO;
import com.peopleops.entity.Skill;
import com.peopleops.repository.SkillRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public SkillResponseDTO createSkill(SkillRequestDTO request) {
        if (skillRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Skill already exists with name: " + request.getName());
        }

        Skill skill = toEntity(request);
        Skill saved = skillRepository.save(skill);
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found with id: " + id));
        return toResponseDTO(skill);
    }

    public SkillResponseDTO updateSkill(Long id, SkillRequestDTO request) {
        Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found with id: " + id));

        if (!skill.getName().equals(request.getName()) && skillRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Skill already exists with name: " + request.getName());
        }

        skill.setName(request.getName());
        skill.setCategory(request.getCategory());

        Skill updated = skillRepository.save(skill);
        return toResponseDTO(updated);
    }

    public void deleteSkill(Long id) {
        Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found with id: " + id));
        skillRepository.delete(skill);
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
