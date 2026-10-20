package com.peopleops.dto;

import com.peopleops.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDocumentRequestDTO {
    private Long employeeId;
    private DocumentType documentType;
    private String fileName;
    private String fileUrl;
}
