package com.unfi.codechallenges.cars.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "car")

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_SEQ")
	@SequenceGenerator(name = "CAR_SEQ", sequenceName = "CAR_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

    @Column(name = "MAKE")
    private String make;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "YEAR_BUILT")
    private String year;

    @Column(name = "VIN")
    private String vin;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(updatable = false, name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "LAST_UPDATED")
    private LocalDateTime lastUpdated;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
        isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
        isActive = true;
    }
}