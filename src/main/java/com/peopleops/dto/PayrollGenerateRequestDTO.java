package com.peopleops.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayrollGenerateRequestDTO {
    private Long employeeId;
    private Integer payMonth;
    private Integer payYear;
}
