package com.peopleops.service;

import com.peopleops.dto.OffboardingRequestDTO;
import com.peopleops.dto.OffboardingResponseDTO;
import com.peopleops.entity.Employee;
import com.peopleops.entity.Offboarding;
import com.peopleops.enums.AssignmentStatus;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.enums.OffboardingStatus;
import com.peopleops.repository.AssetAssignmentRepository;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.OffboardingRepository;
import com.peopleops.repository.PayrollRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OffboardingService {

    private static final Logger log = LoggerFactory.getLogger(OffboardingService.class);

    private final OffboardingRepository offboardingRepository;
    private final EmployeeRepository employeeRepository;
    private final AssetAssignmentRepository assetAssignmentRepository;
    private final PayrollRepository payrollRepository;

    public OffboardingService(
            OffboardingRepository offboardingRepository,
            EmployeeRepository employeeRepository,
            AssetAssignmentRepository assetAssignmentRepository,
            PayrollRepository payrollRepository) {
        this.offboardingRepository = offboardingRepository;
        this.employeeRepository = employeeRepository;
        this.assetAssignmentRepository = assetAssignmentRepository;
        this.payrollRepository = payrollRepository;
    }

    @Transactional
    public OffboardingResponseDTO initiateOffboarding(OffboardingRequestDTO request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + request.getEmployeeId()));

        if (employee.getStatus() != EmployeeStatus.ACTIVE) {
            log.warn("Initiate offboarding rejected: employee id {} is in status {}", employee.getId(), employee.getStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Offboarding can only be initiated for ACTIVE employees. Current status: " + employee.getStatus());
        }

        if (request.getLastWorkingDate().isBefore(request.getResignationDate())) {
            log.warn("Initiate offboarding rejected: last working date {} is before resignation date {}", request.getLastWorkingDate(), request.getResignationDate());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last working date must be on or after resignation date");
        }

        if (offboardingRepository.existsByEmployeeId(request.getEmployeeId())) {
            log.warn("Initiate offboarding rejected: offboarding already exists for employee id {}", request.getEmployeeId());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Offboarding has already been initiated for employee id: " + request.getEmployeeId());
        }

        Offboarding offboarding = toEntity(request);
        offboarding.setStatus(OffboardingStatus.INITIATED);

        employee.setStatus(EmployeeStatus.ON_NOTICE);
        employeeRepository.save(employee);

        Offboarding saved = offboardingRepository.save(offboarding);
        log.info("Initiated offboarding id {} for employee id {}", saved.getId(), saved.getEmployeeId());
        return toResponseDTO(saved);
    }

    public List<OffboardingResponseDTO> getAllOffboardings() {
        List<Offboarding> list = offboardingRepository.findAll();
        List<OffboardingResponseDTO> responses = new ArrayList<>();
        for (Offboarding ob : list) {
            responses.add(toResponseDTO(ob));
        }
        return responses;
    }

    public OffboardingResponseDTO getOffboardingById(Long id) {
        Offboarding offboarding = offboardingRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offboarding record not found with id: " + id));
        return toResponseDTO(offboarding);
    }

    public OffboardingResponseDTO updateOffboarding(Long id, OffboardingRequestDTO request) {
        Offboarding offboarding = offboardingRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offboarding record not found with id: " + id));

        if (offboarding.getStatus() == OffboardingStatus.COMPLETED) {
            log.warn("Update offboarding rejected: offboarding id {} is already COMPLETED", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completed offboarding records cannot be updated");
        }

        if (request.getLastWorkingDate().isBefore(request.getResignationDate())) {
            log.warn("Update offboarding rejected: last working date {} is before resignation date {}", request.getLastWorkingDate(), request.getResignationDate());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last working date must be on or after resignation date");
        }

        offboarding.setResignationDate(request.getResignationDate());
        offboarding.setLastWorkingDate(request.getLastWorkingDate());
        offboarding.setReason(request.getReason());
        offboarding.setExitInterviewNotes(request.getExitInterviewNotes());

        Offboarding updated = offboardingRepository.save(offboarding);
        log.info("Updated offboarding id {}", updated.getId());
        return toResponseDTO(updated);
    }

    @Transactional
    public void deleteOffboarding(Long id) {
        Offboarding offboarding = offboardingRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offboarding record not found with id: " + id));

        if (offboarding.getStatus() == OffboardingStatus.COMPLETED) {
            log.warn("Delete offboarding rejected: offboarding id {} is already COMPLETED", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completed offboarding records cannot be deleted");
        }

        Employee employee = employeeRepository.findById(offboarding.getEmployeeId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + offboarding.getEmployeeId()));
        employee.setStatus(EmployeeStatus.ACTIVE);
        employeeRepository.save(employee);

        offboardingRepository.delete(offboarding);
        log.info("Deleted offboarding id {} and reverted employee id {} to ACTIVE", id, employee.getId());
    }

    @Transactional
    public OffboardingResponseDTO completeOffboarding(Long id) {
        Offboarding offboarding = offboardingRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offboarding record not found with id: " + id));

        if (offboarding.getStatus() == OffboardingStatus.COMPLETED) {
            log.warn("Complete offboarding rejected: offboarding id {} is already COMPLETED", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Offboarding is already completed");
        }

        boolean hasAssignedAssets = assetAssignmentRepository.existsByEmployeeIdAndStatus(offboarding.getEmployeeId(), AssignmentStatus.ASSIGNED);
        if (hasAssignedAssets) {
            log.warn("Complete offboarding rejected: employee id {} has unreturned assets", offboarding.getEmployeeId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot complete offboarding: employee still has assigned assets that must be returned");
        }

        int month = offboarding.getLastWorkingDate().getMonthValue();
        int year = offboarding.getLastWorkingDate().getYear();
        boolean hasPayroll = payrollRepository.existsByEmployeeIdAndPayMonthAndPayYear(offboarding.getEmployeeId(), month, year);
        if (!hasPayroll) {
            log.warn("Complete offboarding rejected: final payroll for {}/{} not found for employee id {}", month, year, offboarding.getEmployeeId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot complete offboarding: final payroll for " + month + "/" + year + " has not been generated");
        }

        offboarding.setStatus(OffboardingStatus.COMPLETED);
        Offboarding updated = offboardingRepository.save(offboarding);

        Employee employee = employeeRepository.findById(offboarding.getEmployeeId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + offboarding.getEmployeeId()));
        employee.setStatus(EmployeeStatus.EXITED);
        employeeRepository.save(employee);

        log.info("Completed offboarding id {} for employee id {}, employee status set to EXITED", updated.getId(), updated.getEmployeeId());
        return toResponseDTO(updated);
    }

    private Offboarding toEntity(OffboardingRequestDTO dto) {
        Offboarding ob = new Offboarding();
        ob.setEmployeeId(dto.getEmployeeId());
        ob.setResignationDate(dto.getResignationDate());
        ob.setLastWorkingDate(dto.getLastWorkingDate());
        ob.setReason(dto.getReason());
        ob.setExitInterviewNotes(dto.getExitInterviewNotes());
        return ob;
    }

    private OffboardingResponseDTO toResponseDTO(Offboarding entity) {
        return new OffboardingResponseDTO(
            entity.getId(),
            entity.getEmployeeId(),
            entity.getResignationDate(),
            entity.getLastWorkingDate(),
            entity.getReason(),
            entity.getExitInterviewNotes(),
            entity.getStatus()
        );
    }
}
