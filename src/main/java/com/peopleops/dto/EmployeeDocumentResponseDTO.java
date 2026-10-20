package com.peopleops.dto;

import com.peopleops.enums.DocumentType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDocumentResponseDTO {
    private Long id;
    private Long employeeId;
    private DocumentType documentType;
    private String fileName;
    private String fileUrl;
    private LocalDate uploadedOn;
    private Boolean verified;
}
