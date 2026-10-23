package com.peopleops.service;

import com.peopleops.dto.LeaveTypeRequestDTO;
import com.peopleops.dto.LeaveTypeResponseDTO;
import com.peopleops.entity.LeaveType;
import com.peopleops.exception.DuplicateResourceException;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.LeaveTypeRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LeaveTypeService {

    private static final Logger log = LoggerFactory.getLogger(LeaveTypeService.class);

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

    public LeaveTypeResponseDTO createLeaveType(LeaveTypeRequestDTO request) {
        if (leaveTypeRepository.existsByName(request.getName())) {
            log.warn("Leave type creation failed: duplicate name '{}'", request.getName());
            throw new DuplicateResourceException("Leave type already exists with name: " + request.getName());
        }

        LeaveType leaveType = toEntity(request);
        LeaveType saved = leaveTypeRepository.save(leaveType);
        log.info("Created leave type with id: {} and name: '{}'", saved.getId(), saved.getName());
        return toResponseDTO(saved);
    }

    public List<LeaveTypeResponseDTO> getAllLeaveTypes() {
        List<LeaveType> list = leaveTypeRepository.findAll();
        List<LeaveTypeResponseDTO> responses = new ArrayList<>();
        for (LeaveType lt : list) {
            responses.add(toResponseDTO(lt));
        }
        return responses;
    }

    public LeaveTypeResponseDTO getLeaveTypeById(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Leave type lookup failed: id {} not found", id);
                return new ResourceNotFoundException("Leave type not found with id: " + id);
            });
        return toResponseDTO(leaveType);
    }

    public LeaveTypeResponseDTO updateLeaveType(Long id, LeaveTypeRequestDTO request) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Leave type update failed: id {} not found", id);
                return new ResourceNotFoundException("Leave type not found with id: " + id);
            });

        if (!leaveType.getName().equals(request.getName()) && leaveTypeRepository.existsByName(request.getName())) {
            log.warn("Leave type update failed for id {}: duplicate name '{}'", id, request.getName());
            throw new DuplicateResourceException("Leave type already exists with name: " + request.getName());
        }

        leaveType.setName(request.getName());
        leaveType.setAnnualQuota(request.getAnnualQuota());
        leaveType.setPaid(request.getPaid());

        LeaveType updated = leaveTypeRepository.save(leaveType);
        log.info("Updated leave type id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteLeaveType(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Leave type delete failed: id {} not found", id);
                return new ResourceNotFoundException("Leave type not found with id: " + id);
            });
        leaveTypeRepository.delete(leaveType);
        log.info("Deleted leave type id: {}", id);
    }

    private LeaveType toEntity(LeaveTypeRequestDTO dto) {
        LeaveType lt = new LeaveType();
        lt.setName(dto.getName());
        lt.setAnnualQuota(dto.getAnnualQuota());
        lt.setPaid(dto.getPaid());
        return lt;
    }

    private LeaveTypeResponseDTO toResponseDTO(LeaveType entity) {
        return new LeaveTypeResponseDTO(
            entity.getId(),
            entity.getName(),
            entity.getAnnualQuota(),
            entity.getPaid()
        );
    }
}
