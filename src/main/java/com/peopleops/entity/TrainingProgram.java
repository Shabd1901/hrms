package com.peopleops.entity;

import com.peopleops.enums.TrainingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TrainingProgram defines corporate training courses offered to upskill employees
@Entity
@Table(name = "training_programs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "trainer_name", nullable = false, length = 100)
    private String trainerName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TrainingStatus status;

    // R19: TrainingProgram <-> Skill (N-N with join table training_program_skills)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "training_program_skills",
        joinColumns = @JoinColumn(name = "training_program_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();
}