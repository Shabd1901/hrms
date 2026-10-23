package com.peopleops.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.peopleops.dto.PayrollGenerateRequestDTO;
import com.peopleops.dto.PayrollResponseDTO;
import com.peopleops.service.PayrollService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payrolls", description = "Monthly payroll generation and payment processing APIs")
@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping("/generate")
    public ResponseEntity<PayrollResponseDTO> generatePayroll(@Valid @RequestBody PayrollGenerateRequestDTO request) {
        PayrollResponseDTO response = payrollService.generatePayroll(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PayrollResponseDTO>> getAllPayrolls(
            @RequestParam(required = false) Long employeeId) {
        List<PayrollResponseDTO> list = payrollService.getAllPayrolls(employeeId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollResponseDTO> getPayrollById(@PathVariable Long id) {
        PayrollResponseDTO response = payrollService.getPayrollById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(@PathVariable Long id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<PayrollResponseDTO> payPayroll(@PathVariable Long id) {
        PayrollResponseDTO response = payrollService.payPayroll(id);
        return ResponseEntity.ok(response);
    }
}
