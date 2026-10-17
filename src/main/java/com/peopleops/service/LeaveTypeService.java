package com.peopleops.service;

import com.peopleops.dto.LeaveTypeRequestDTO;
import com.peopleops.dto.LeaveTypeResponseDTO;
import com.peopleops.entity.LeaveType;
import com.peopleops.repository.LeaveTypeRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

    public LeaveTypeResponseDTO createLeaveType(LeaveTypeRequestDTO request) {
        if (leaveTypeRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Leave type already exists with name: " + request.getName());
        }

        LeaveType leaveType = toEntity(request);
        LeaveType saved = leaveTypeRepository.save(leaveType);
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave type not found with id: " + id));
        return toResponseDTO(leaveType);
    }

    public LeaveTypeResponseDTO updateLeaveType(Long id, LeaveTypeRequestDTO request) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave type not found with id: " + id));

        if (!leaveType.getName().equals(request.getName()) && leaveTypeRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Leave type already exists with name: " + request.getName());
        }

        leaveType.setName(request.getName());
        leaveType.setAnnualQuota(request.getAnnualQuota());
        leaveType.setPaid(request.getPaid());

        LeaveType updated = leaveTypeRepository.save(leaveType);
        return toResponseDTO(updated);
    }

    public void deleteLeaveType(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave type not found with id: " + id));
        leaveTypeRepository.delete(leaveType);
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
