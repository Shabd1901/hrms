package com.peopleops.entity;

import com.peopleops.enums.OffboardingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Offboarding manages employee resignation, notice period, and exit clearance
@Entity
@Table(name = "offboardings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"employee_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offboarding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // R16: Offboarding -> Employee (1-1, unique foreign key on employee_id)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    @Column(name = "resignation_date", nullable = false)
    private LocalDate resignationDate;

    @Column(name = "last_working_date", nullable = false)
    private LocalDate lastWorkingDate;

    @Column(name = "reason", length = 1000)
    private String reason;

    @Column(name = "exit_interview_notes", length = 1000)
    private String exitInterviewNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OffboardingStatus status;
}
