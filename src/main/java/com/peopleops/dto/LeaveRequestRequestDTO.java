package com.peopleops.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestRequestDTO {
    private Long employeeId;
    private Long leaveTypeId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
}
