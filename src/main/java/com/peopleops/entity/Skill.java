package com.peopleops.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Skill represents a technical or professional competency catalog item
@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique competency name (e.g., "Java", "Docker", "Leadership")
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    // Classification group (e.g., "Technical", "Management", "Domain")
    @Column(name = "category", nullable = false, length = 100)
    private String category;
}
