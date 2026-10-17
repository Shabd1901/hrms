package com.peopleops.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveTypeRequestDTO {
    private String name;
    private Integer annualQuota;
    private Boolean paid;
}
