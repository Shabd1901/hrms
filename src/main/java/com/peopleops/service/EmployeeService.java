package com.peopleops.service;

import com.peopleops.dto.EmployeeRequestDTO;
import com.peopleops.dto.EmployeeResponseDTO;
import com.peopleops.dto.HireRequestDTO;
import com.peopleops.entity.Candidate;
import com.peopleops.entity.Employee;
import com.peopleops.entity.JobOpening;
import com.peopleops.entity.OnboardingTask;
import com.peopleops.enums.EmployeeStatus;
import com.peopleops.enums.OnboardingTaskStatus;
import com.peopleops.repository.DepartmentRepository;
import com.peopleops.repository.DesignationRepository;
import com.peopleops.repository.EmployeeRepository;
import com.peopleops.repository.OnboardingTaskRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final OnboardingTaskRepository onboardingTaskRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            DesignationRepository designationRepository,
            OnboardingTaskRepository onboardingTaskRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
        this.onboardingTaskRepository = onboardingTaskRepository;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists: " + request.getEmail());
        }
        if (!departmentRepository.existsById(request.getDepartmentId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + request.getDepartmentId());
        }
        if (!designationRepository.existsById(request.getDesignationId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + request.getDesignationId());
        }
        if (request.getManagerId() != null && !employeeRepository.existsById(request.getManagerId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found with id: " + request.getManagerId());
        }

        long nextId = employeeRepository.findMaxId().orElse(0L) + 1;
        String employeeCode = String.format("EMP-%04d", nextId);

        Employee employee = toEntity(request);
        employee.setEmployeeCode(employeeCode);
        employee.setStatus(EmployeeStatus.ONBOARDING);

        Employee saved = employeeRepository.save(employee);
        generateDefaultOnboardingTasks(saved);
        return toResponseDTO(saved);
    }

    @Transactional
    public EmployeeResponseDTO createFromCandidate(Candidate candidate, JobOpening opening, HireRequestDTO hireRequest) {
        if (employeeRepository.existsByEmail(candidate.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists: " + candidate.getEmail());
        }
        if (hireRequest.getManagerId() != null && !employeeRepository.existsById(hireRequest.getManagerId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found with id: " + hireRequest.getManagerId());
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id));
        return toResponseDTO(employee);
    }

    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO request) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id));

        if (!employee.getEmail().equals(request.getEmail()) && employeeRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists: " + request.getEmail());
        }
        if (!departmentRepository.existsById(request.getDepartmentId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + request.getDepartmentId());
        }
        if (!designationRepository.existsById(request.getDesignationId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + request.getDesignationId());
        }
        if (request.getManagerId() != null) {
            if (request.getManagerId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An employee cannot be their own manager");
            }
            if (!employeeRepository.existsById(request.getManagerId())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found with id: " + request.getManagerId());
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
        return toResponseDTO(updated);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id));

        if (employee.getStatus() != EmployeeStatus.ONBOARDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee can only be deleted while in ONBOARDING status");
        }

        onboardingTaskRepository.deleteByEmployeeId(id);
        employeeRepository.delete(employee);
    }

    public EmployeeResponseDTO transferEmployee(Long id, Long departmentId) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id));

        if (employee.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee is exited and cannot be transferred");
        }
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id: " + departmentId);
        }

        employee.setDepartmentId(departmentId);
        Employee updated = employeeRepository.save(employee);
        return toResponseDTO(updated);
    }

    public EmployeeResponseDTO promoteEmployee(Long id, Long designationId) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id));

        if (employee.getStatus() == EmployeeStatus.EXITED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee is exited and cannot be promoted");
        }
        if (!designationRepository.existsById(designationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Designation not found with id: " + designationId);
        }

        employee.setDesignationId(designationId);
        Employee updated = employeeRepository.save(employee);
        return toResponseDTO(updated);
    }

    public void activateIfReady(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null || employee.getStatus() != EmployeeStatus.ONBOARDING) {
            return;
        }

        long totalTasks = onboardingTaskRepository.countByEmployeeId(employeeId);
        long doneTasks = onboardingTaskRepository.countByEmployeeIdAndStatus(employeeId, OnboardingTaskStatus.DONE);

        if (totalTasks > 0 && totalTasks == doneTasks) {
            employee.setStatus(EmployeeStatus.ACTIVE);
            employeeRepository.save(employee);
        }
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
