package com.peopleops.dto;

import com.peopleops.enums.AssignmentStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetAssignmentResponseDTO {
    private Long id;
    private Long assetId;
    private Long employeeId;
    private LocalDate assignedDate;
    private LocalDate returnedDate;
    private AssignmentStatus status;
}
