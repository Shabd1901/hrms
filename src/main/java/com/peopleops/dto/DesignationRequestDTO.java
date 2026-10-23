package com.peopleops.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DesignationRequestDTO {

    @NotBlank(message = "Designation title is required")
    private String title;

    @NotNull(message = "Grade level is required")
    @Min(value = 1, message = "Grade level must be at least 1")
    private Integer gradeLevel;

    private String description;
}
