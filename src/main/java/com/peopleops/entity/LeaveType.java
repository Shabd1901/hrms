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

// LeaveType defines company leave policies (e.g., Casual Leave, Sick Leave)
@Entity
@Table(name = "leave_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique policy name (e.g., "Sick Leave") preventing duplicate categories
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    // Total days granted per calendar year for this leave category
    @Column(name = "annual_quota", nullable = false)
    private Integer annualQuota;

    // true = salary paid during leave; false = unpaid leave (triggers LOP deduction)
    @Column(name = "paid", nullable = false)
    private Boolean paid;
}
