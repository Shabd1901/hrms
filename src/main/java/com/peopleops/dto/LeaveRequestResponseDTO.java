package com.peopleops.dto;

import com.peopleops.enums.LeaveRequestStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestResponseDTO {
    private Long id;
    private Long employeeId;
    private Long leaveTypeId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer days;
    private String reason;
    private LeaveRequestStatus status;
    private Long decidedById;
    private LocalDate decidedOn;
}
