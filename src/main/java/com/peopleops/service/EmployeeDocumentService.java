package com.peopleops.service;

import com.peopleops.dto.EmployeeDocumentRequestDTO;
import com.peopleops.dto.EmployeeDocumentResponseDTO;
import com.peopleops.entity.Employee;
import com.peopleops.entity.EmployeeDocument;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.EmployeeDocumentRepository;
import com.peopleops.repository.EmployeeRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeDocumentService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeDocumentService.class);

    private final EmployeeDocumentRepository employeeDocumentRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    public EmployeeDocumentService(
            EmployeeDocumentRepository employeeDocumentRepository,
            EmployeeRepository employeeRepository,
            EmployeeService employeeService) {
        this.employeeDocumentRepository = employeeDocumentRepository;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    public EmployeeDocumentResponseDTO createDocument(EmployeeDocumentRequestDTO request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> {
                log.warn("Create document rejected: employee id {} not found", request.getEmployeeId());
                return new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId());
            });

        EmployeeDocument document = toEntity(request);
        document.setEmployee(employee);
        document.setUploadedOn(LocalDate.now());
        document.setVerified(false);

        EmployeeDocument saved = employeeDocumentRepository.save(document);
        log.info("Uploaded document '{}' ({}) for employee id {} with doc id {}", saved.getFileName(), saved.getDocumentType(), saved.getEmployee() != null ? saved.getEmployee().getId() : null, saved.getId());
        return toResponseDTO(saved);
    }

    public List<EmployeeDocumentResponseDTO> getAllDocuments(Long employeeId) {
        List<EmployeeDocument> list = (employeeId != null)
            ? employeeDocumentRepository.findByEmployeeId(employeeId)
            : employeeDocumentRepository.findAll();

        List<EmployeeDocumentResponseDTO> responses = new ArrayList<>();
        for (EmployeeDocument doc : list) {
            responses.add(toResponseDTO(doc));
        }
        return responses;
    }

    public EmployeeDocumentResponseDTO getDocumentById(Long id) {
        EmployeeDocument document = employeeDocumentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee document not found with id: " + id));
        return toResponseDTO(document);
    }

    public EmployeeDocumentResponseDTO updateDocument(Long id, EmployeeDocumentRequestDTO request) {
        EmployeeDocument document = employeeDocumentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee document not found with id: " + id));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> {
                log.warn("Update document rejected: employee id {} not found", request.getEmployeeId());
                return new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId());
            });

        document.setEmployee(employee);
        document.setDocumentType(request.getDocumentType());
        document.setFileName(request.getFileName());
        document.setFileUrl(request.getFileUrl());

        EmployeeDocument updated = employeeDocumentRepository.save(document);
        log.info("Updated document id {}", updated.getId());
        return toResponseDTO(updated);
    }

    public void deleteDocument(Long id) {
        EmployeeDocument document = employeeDocumentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee document not found with id: " + id));
        employeeDocumentRepository.delete(document);
        log.info("Deleted document id {}", id);
    }

    @Transactional
    public EmployeeDocumentResponseDTO verifyDocument(Long id) {
        EmployeeDocument document = employeeDocumentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee document not found with id: " + id));

        document.setVerified(true);
        EmployeeDocument updated = employeeDocumentRepository.save(document);
        Long empId = updated.getEmployee() != null ? updated.getEmployee().getId() : null;
        log.info("Verified document id {} for employee id {}", updated.getId(), empId);

        if (empId != null) {
            employeeService.activateIfReady(empId);
        }

        return toResponseDTO(updated);
    }

    private EmployeeDocument toEntity(EmployeeDocumentRequestDTO dto) {
        EmployeeDocument doc = new EmployeeDocument();
        doc.setDocumentType(dto.getDocumentType());
        doc.setFileName(dto.getFileName());
        doc.setFileUrl(dto.getFileUrl());
        return doc;
    }

    private EmployeeDocumentResponseDTO toResponseDTO(EmployeeDocument entity) {
        return new EmployeeDocumentResponseDTO(
            entity.getId(),
            entity.getEmployee() != null ? entity.getEmployee().getId() : null,
            entity.getDocumentType(),
            entity.getFileName(),
            entity.getFileUrl(),
            entity.getUploadedOn(),
            entity.getVerified()
        );
    }
}
