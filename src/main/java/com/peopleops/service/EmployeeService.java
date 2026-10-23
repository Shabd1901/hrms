package com.peopleops.service;

import com.peopleops.dto.EmployeeRequestDTO;
import com.peopleops.dto.EmployeeResponseDTO;
import com.peopleops.dto.HireRequestDTO;
import com.peopleops.entity.Candidate;
import com.peopleops.entity.Employee;
import com.peopleops.entity.JobOpening;
import com.peopleops.entity.LeaveBalance;
import com.peopleops.entity.LeaveType;
import com.peopleops.entity.OnboardingTask;
import com.peopleops.enums.DocumentType;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.enums.OnboardingTaskStatus;
import com.peopleops.exception.BusinessRuleException;
import com.peopleops.exception.DuplicateResourceException;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.DepartmentRepository;
import com.peopleops.repository.DesignationRepository;
import com.peopleops.repository.EmployeeDocumentRepository;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.LeaveBalanceRepository;
import com.peopleops.repository.LeaveTypeRepository;
import com.peopleops.repository.OnboardingTaskRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final OnboardingTaskRepository onboardingTaskRepository;
    private final EmployeeDocumentRepository employeeDocumentRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            DesignationRepository designationRepository,
            OnboardingTaskRepository onboardingTaskRepository,
            EmployeeDocumentRepository employeeDocumentRepository,
            LeaveTypeRepository leaveTypeRepository,
            LeaveBalanceRepository leaveBalanceRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
        this.onboardingTaskRepository = onboardingTaskRepository;
        this.employeeDocumentRepository = employeeDocumentRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            log.warn("Employee creation failed: duplicate email '{}'", request.getEmail());
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        if (!departmentRepository.existsById(request.getDepartmentId())) {
            log.warn("Employee creation failed: department id {} not found", request.getDepartmentId());
            throw new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId());
        }
        if (!designationRepository.existsById(request.getDesignationId())) {
            log.warn("Employee creation failed: designation id {} not found", request.getDesignationId());
            throw new ResourceNotFoundException("Designation not found with id: " + request.getDesignationId());
        }
        if (request.getManagerId() != null && !employeeRepository.existsById(request.getManagerId())) {
            log.warn("Employee creation failed: manager id {} not found", request.getManagerId());
            throw new ResourceNotFoundException("Manager not found with id: " + request.getManagerId());
        }

        long nextId = employeeRepository.findMaxId().orElse(0L) + 1;
        String employeeCode = String.format("EMP-%04d", nextId);

        Employee employee = toEntity(request);
        employee.setEmployeeCode(employeeCode);
        employee.setStatus(EmployeeStatus.ONBOARDING);

        Employee saved = employeeRepository.save(employee);
        log.info("Created employee with code: '{}' and id: {}", saved.getEmployeeCode(), saved.getId());
        generateDefaultOnboardingTasks(saved);
        return toResponseDTO(saved);
    }

    @Transactional
    public EmployeeResponseDTO createFromCandidate(Candidate candidate, JobOpening opening, HireRequestDTO hireRequest) {
        if (employeeRepository.existsByEmail(candidate.getEmail())) {
            log.warn("Hiring candidate failed: duplicate email '{}'", candidate.getEmail());
            throw new DuplicateResourceException("Email already exists: " + candidate.getEmail());
        }
        if (hireRequest.getManagerId() != null && !employeeRepository.existsById(hireRequest.getManagerId())) {
            log.warn("Hiring candidate failed: manager id {} not found", hireRequest.getManagerId());
            throw new ResourceNotFoundException("Manager not found with id: " + hireRequest.getManagerId());
        }

        long nextId = employeeRepository.findMaxId().orElse(0L) + 1;
        String employeeCode = String.format("EMP-%04d", nextId);

        Employee employee = new Employee();
        employee.setEmployeeCode(employeeCode);
        employee.setFirstName(candidate.getFirstName());
        employee.setLastName(candidate.getLastName());
        employee.setEmail(candidate.getEmail());
        employee.setPhone(candidate.getPhone());
        employee.setDateOfJoining(hireRequest.getDateOfJoining());
        employee.setMonthlySalary(hireRequest.getMonthlySalary());
        employee.setDepartmentId(opening.getDepartmentId());
        employee.setDesignationId(opening.getDesignationId());
        employee.setManagerId(hireRequest.getManagerId());
        employee.setStatus(EmployeeStatus.ONBOARDING);

        Employee saved = employeeRepository.save(employee);
        log.info("Hired candidate id {} as employee with code: '{}' and id: {}", candidate.getId(), saved.getEmployeeCode(), saved.getId());
        generateDefaultOnboardingTasks(saved);
        return toResponseDTO(saved);
    }

    public List<EmployeeResponseDTO> getAllEmployees(Long departmentId, EmployeeStatus status) {
        List<Employee> employees;
        if (departmentId != null && status != null) {
            employees = employeeRepository.findByDepartmentIdAndStatus(departmentId, status);
        } else if (departmentId != null) {
            employees = employeeRepository.findByDepartmentId(departmentId);
        } else if (status != null) {
            employees = employeeRepository.findByStatus(status);
        } else {
            employees = employeeRepository.findAll();
        }

        List<EmployeeResponseDTO> responses = new ArrayList<>();
        for (Employee emp : employees) {
            responses.add(toResponseDTO(emp));
        }
        return responses;
    }

    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Employee lookup failed: id {} not found", id);
                return new ResourceNotFoundException("Employee not found with id: " + id);
            });
        return toResponseDTO(employee);
    }

    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO request) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Employee update failed: id {} not found", id);
                return new ResourceNotFoundException("Employee not found with id: " + id);
            });

        if (!employee.getEmail().equals(request.getEmail()) && employeeRepository.existsByEmail(request.getEmail())) {
            log.warn("Employee update failed for id {}: duplicate email '{}'", id, request.getEmail());
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        if (!departmentRepository.existsById(request.getDepartmentId())) {
            log.warn("Employee update failed for id {}: department id {} not found", id, request.getDepartmentId());
            throw new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId());
        }
        if (!designationRepository.existsById(request.getDesignationId())) {
            log.warn("Employee update failed for id {}: designation id {} not found", id, request.getDesignationId());
            throw new ResourceNotFoundException("Designation not found with id: " + request.getDesignationId());
        }
        if (request.getManagerId() != null) {
            if (request.getManagerId().equals(id)) {
                log.warn("Employee update failed for id {}: cannot be own manager", id);
                throw new BusinessRuleException("An employee cannot be their own manager");
            }
            if (!employeeRepository.existsById(request.getManagerId())) {
                log.warn("Employee update failed for id {}: manager id {} not found", id, request.getManagerId());
                throw new ResourceNotFoundException("Manager not found with id: " + request.getManagerId());
            }
        }

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setDateOfJoining(request.getDateOfJoining());
        employee.setMonthlySalary(request.getMonthlySalary());
        employee.setDepartmentId(request.getDepartmentId());
        employee.setDesignationId(request.getDesignationId());
        employee.setManagerId(request.getManagerId());

        Employee updated = employeeRepository.save(employee);
        log.info("Updated employee id: {}", updated.getId());
        return toResponseDTO(updated);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Employee delete failed: id {} not found", id);
                return new ResourceNotFoundException("Employee not found with id: " + id);
            });

        if (employee.getStatus() != EmployeeStatus.ONBOARDING) {
            log.warn("Employee delete failed: id {} is in {} status (must be ONBOARDING)", id, employee.getStatus());
            throw new BusinessRuleException("Employee can only be deleted while in ONBOARDING status");
        }

        onboardingTaskRepository.deleteByEmployeeId(id);
        employeeRepository.delete(employee);
        log.info("Deleted onboarding employee id: {}", id);
    }

    public EmployeeResponseDTO transferEmployee(Long id, Long departmentId) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Employee transfer failed: id {} not found", id);
                return new ResourceNotFoundException("Employee not found with id: " + id);
            });

        if (employee.getStatus() == EmployeeStatus.EXITED) {
            log.warn("Employee transfer failed: id {} is EXITED", id);
            throw new BusinessRuleException("Employee is exited and cannot be transferred");
        }
        if (!departmentRepository.existsById(departmentId)) {
            log.warn("Employee transfer failed: target department id {} not found", departmentId);
            throw new ResourceNotFoundException("Department not found with id: " + departmentId);
        }

        employee.setDepartmentId(departmentId);
        Employee updated = employeeRepository.save(employee);
        log.info("Transferred employee id: {} to department id: {}", id, departmentId);
        return toResponseDTO(updated);
    }

    public EmployeeResponseDTO promoteEmployee(Long id, Long designationId) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Employee promotion failed: id {} not found", id);
                return new ResourceNotFoundException("Employee not found with id: " + id);
            });

        if (employee.getStatus() == EmployeeStatus.EXITED) {
            log.warn("Employee promotion failed: id {} is EXITED", id);
            throw new BusinessRuleException("Employee is exited and cannot be promoted");
        }
        if (!designationRepository.existsById(designationId)) {
            log.warn("Employee promotion failed: target designation id {} not found", designationId);
            throw new ResourceNotFoundException("Designation not found with id: " + designationId);
        }

        employee.setDesignationId(designationId);
        Employee updated = employeeRepository.save(employee);
        log.info("Promoted employee id: {} to designation id: {}", id, designationId);
        return toResponseDTO(updated);
    }

    @Transactional
    public void activateIfReady(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null || employee.getStatus() != EmployeeStatus.ONBOARDING) {
            return;
        }

        long totalTasks = onboardingTaskRepository.countByEmployeeId(employeeId);
        long doneTasks = onboardingTaskRepository.countByEmployeeIdAndStatus(employeeId, OnboardingTaskStatus.DONE);

        if (totalTasks == 0 || totalTasks != doneTasks) {
            return;
        }

        boolean hasVerifiedIdProof = employeeDocumentRepository.existsByEmployeeIdAndDocumentTypeAndVerifiedTrue(employeeId, DocumentType.ID_PROOF);
        if (!hasVerifiedIdProof) {
            return;
        }

        employee.setStatus(EmployeeStatus.ACTIVE);
        employeeRepository.save(employee);
        log.info("Activated employee id: {} to ACTIVE status", employeeId);

        int currentYear = LocalDate.now().getYear();
        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();
        for (LeaveType leaveType : leaveTypes) {
            if (!leaveBalanceRepository.existsByEmployeeIdAndLeaveTypeIdAndBalanceYear(employeeId, leaveType.getId(), currentYear)) {
                LeaveBalance balance = new LeaveBalance();
                balance.setEmployeeId(employeeId);
                balance.setLeaveTypeId(leaveType.getId());
                balance.setBalanceYear(currentYear);
                balance.setTotalDays(leaveType.getAnnualQuota());
                balance.setUsedDays(0);
                leaveBalanceRepository.save(balance);
            }
        }
        log.info("Seeded initial leave balances for newly activated employee id: {}", employeeId);
    }

    private void generateDefaultOnboardingTasks(Employee employee) {
        LocalDate dueDate = (employee.getDateOfJoining() != null)
            ? employee.getDateOfJoining().plusDays(7)
            : LocalDate.now().plusDays(7);

        String[][] defaultTasks = {
            {"Submit Identity Documents", "Submit government-issued ID proofs"},
            {"Sign Offer/Contract", "Sign the employment contract and agreements"},
            {"Collect Work Asset", "Collect laptop and assigned office equipment"},
            {"HR Orientation", "Complete initial company orientation session"},
            {"Set Up Work Email", "Configure company email and system access"}
        };

        for (String[] taskData : defaultTasks) {
            OnboardingTask task = new OnboardingTask();
            task.setEmployeeId(employee.getId());
            task.setTitle(taskData[0]);
            task.setDescription(taskData[1]);
            task.setDueDate(dueDate);
            task.setStatus(OnboardingTaskStatus.PENDING);
            onboardingTaskRepository.save(task);
        }
    }

    private Employee toEntity(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setDateOfJoining(dto.getDateOfJoining());
        employee.setMonthlySalary(dto.getMonthlySalary());
        employee.setDepartmentId(dto.getDepartmentId());
        employee.setDesignationId(dto.getDesignationId());
        employee.setManagerId(dto.getManagerId());
        return employee;
    }

    private EmployeeResponseDTO toResponseDTO(Employee entity) {
        return new EmployeeResponseDTO(
            entity.getId(),
            entity.getEmployeeCode(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getDateOfBirth(),
            entity.getDateOfJoining(),
            entity.getMonthlySalary(),
            entity.getStatus(),
            entity.getDepartmentId(),
            entity.getDesignationId(),
            entity.getManagerId()
        );
    }
}
