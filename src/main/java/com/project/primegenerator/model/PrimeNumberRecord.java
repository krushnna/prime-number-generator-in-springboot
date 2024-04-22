package com.project.primegenerator.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
public class PrimeNumberRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime timestamp;
    private int startRange;
    private int endRange;
    private String algorithm;
    private int numberOfPrimes;
    private long timeTaken;
    private String timeComplexity;
}
