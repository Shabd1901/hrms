package com.peopleops.service;

import com.peopleops.dto.AssetAssignmentRequestDTO;
import com.peopleops.dto.AssetAssignmentResponseDTO;
import com.peopleops.entity.Asset;
import com.peopleops.entity.AssetAssignment;
import com.peopleops.entity.Employee;
import com.peopleops.enums.AssetStatus;
import com.peopleops.enums.AssignmentStatus;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.exception.BusinessRuleException;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.AssetAssignmentRepository;
import com.peopleops.repository.AssetRepository;
import com.peopleops.repository.EmployeeRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssetAssignmentService {

    private static final Logger log = LoggerFactory.getLogger(AssetAssignmentService.class);

    private final AssetAssignmentRepository assetAssignmentRepository;
    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;

    public AssetAssignmentService(
            AssetAssignmentRepository assetAssignmentRepository,
            AssetRepository assetRepository,
            EmployeeRepository employeeRepository) {
        this.assetAssignmentRepository = assetAssignmentRepository;
        this.assetRepository = assetRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public AssetAssignmentResponseDTO assignAsset(AssetAssignmentRequestDTO request) {
        Asset asset = assetRepository.findById(request.getAssetId())
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + request.getAssetId()));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        if (employee.getStatus() != EmployeeStatus.ONBOARDING && employee.getStatus() != EmployeeStatus.ACTIVE) {
            log.warn("Assign asset rejected: employee {} status is {}", employee.getId(), employee.getStatus());
            throw new BusinessRuleException("Assets can only be assigned to ONBOARDING or ACTIVE employees. Employee status: " + employee.getStatus());
        }

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            log.warn("Assign asset rejected: asset {} status is {}", asset.getId(), asset.getStatus());
            throw new BusinessRuleException("Only AVAILABLE assets can be assigned. Asset status: " + asset.getStatus());
        }

        AssetAssignment assignment = new AssetAssignment();
        assignment.setAsset(asset);
        assignment.setEmployee(employee);
        assignment.setAssignedDate(request.getAssignedDate() != null ? request.getAssignedDate() : LocalDate.now());
        assignment.setStatus(AssignmentStatus.ASSIGNED);
        assignment.setReturnedDate(null);

        asset.setStatus(AssetStatus.ASSIGNED);
        assetRepository.save(asset);

        AssetAssignment saved = assetAssignmentRepository.save(assignment);
        log.info("Assigned asset id {} to employee id {} (assignment id {})", asset.getId(), employee.getId(), saved.getId());
        return toResponseDTO(saved);
    }

    public List<AssetAssignmentResponseDTO> getAllAssignments(Long employeeId) {
        List<AssetAssignment> list = (employeeId != null)
            ? assetAssignmentRepository.findByEmployeeId(employeeId)
            : assetAssignmentRepository.findAll();

        List<AssetAssignmentResponseDTO> responses = new ArrayList<>();
        for (AssetAssignment aa : list) {
            responses.add(toResponseDTO(aa));
        }
        return responses;
    }

    public AssetAssignmentResponseDTO getAssignmentById(Long id) {
        AssetAssignment assignment = assetAssignmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset assignment not found with id: " + id));
        return toResponseDTO(assignment);
    }

    @Transactional
    public AssetAssignmentResponseDTO returnAsset(Long id) {
        AssetAssignment assignment = assetAssignmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset assignment not found with id: " + id));

        if (assignment.getStatus() != AssignmentStatus.ASSIGNED) {
            log.warn("Return asset rejected: assignment id {} status is {}", id, assignment.getStatus());
            throw new BusinessRuleException("Only ASSIGNED assets can be returned. Current status: " + assignment.getStatus());
        }

        assignment.setStatus(AssignmentStatus.RETURNED);
        assignment.setReturnedDate(LocalDate.now());

        Asset asset = assignment.getAsset();
        if (asset == null) {
            log.warn("Return asset rejected: asset not found on assignment {}", id);
            throw new ResourceNotFoundException("Asset not found on assignment id: " + id);
        }
        asset.setStatus(AssetStatus.AVAILABLE);
        assetRepository.save(asset);

        AssetAssignment updated = assetAssignmentRepository.save(assignment);
        log.info("Returned asset id {} from assignment id {}", asset.getId(), id);
        return toResponseDTO(updated);
    }

    private AssetAssignmentResponseDTO toResponseDTO(AssetAssignment entity) {
        return new AssetAssignmentResponseDTO(
            entity.getId(),
            entity.getAsset() != null ? entity.getAsset().getId() : null,
            entity.getEmployee() != null ? entity.getEmployee().getId() : null,
            entity.getAssignedDate(),
            entity.getReturnedDate(),
            entity.getStatus()
        );
    }
}
