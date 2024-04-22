package com.project.primegenerator.repository;

import com.project.primegenerator.model.PrimeNumberRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrimeNumberRepository extends JpaRepository<PrimeNumberRecord,Long> {
    Optional<PrimeNumberRecord> findById(Long id);

    //ACID properties following like transactional annotation used
}
