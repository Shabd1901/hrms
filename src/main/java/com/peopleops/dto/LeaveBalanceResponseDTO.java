package com.peopleops.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceResponseDTO {
    private Long id;
    private Long employeeId;
    private Long leaveTypeId;
    private Integer balanceYear;
    private Integer totalDays;
    private Integer usedDays;
}
