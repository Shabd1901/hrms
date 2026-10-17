package com.peopleops.dto;

import com.peopleops.enums.EmployeeStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private LocalDate dateOfJoining;
    private BigDecimal monthlySalary;
    private EmployeeStatus status;
    private Long departmentId;
    private Long designationId;
    private Long managerId;
}
