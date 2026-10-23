package com.peopleops.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetAssignmentRequestDTO {

    @NotNull(message = "Asset id is required")
    private Long assetId;

    @NotNull(message = "Employee id is required")
    private Long employeeId;

    @NotNull(message = "Assigned date is required")
    private LocalDate assignedDate;
}
