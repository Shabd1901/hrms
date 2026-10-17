package com.peopleops.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DesignationResponseDTO {
    private Long id;
    private String title;
    private Integer gradeLevel;
    private String description;
}
