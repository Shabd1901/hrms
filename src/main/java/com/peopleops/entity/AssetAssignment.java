package com.peopleops.entity;

import com.peopleops.enums.AssignmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// AssetAssignment tracks the allocation and return lifecycle of company hardware to employees
@Entity
@Table(name = "asset_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    // Date when employee returned hardware during offboarding or replacement; null while ASSIGNED
    @Column(name = "returned_date")
    private LocalDate returnedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AssignmentStatus status;
}
