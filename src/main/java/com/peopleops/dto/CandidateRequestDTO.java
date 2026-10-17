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
public class CandidateRequestDTO {
    private Long jobOpeningId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String resumeNotes;
    private LocalDate appliedDate;
}
