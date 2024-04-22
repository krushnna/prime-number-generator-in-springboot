package com.project.primegenerator.controller;


import com.project.primegenerator.model.PrimeNumberRecord;
import com.project.primegenerator.service.PrimeNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private PrimeNumberService service;
    // POST endpoint for user who provides starting num , ending num and type of algorithm
    /*POST- api/generate-primenum
    request parameters int start,int end and String algorithm
    types of algorithm 1.SieveOfEratosthenes, 2.SieveOfAtkin, 3.TrialDivision
     */

    @PostMapping("/generate-primenum")
    public ResponseEntity<List<Integer>> generatePrimes(@RequestParam int start, @RequestParam int end, @RequestParam String algorithm) {
        Optional<List<Integer>> primesOptional = service.generatePrimes(start, end,algorithm);
        if (primesOptional.isPresent()) {
            return ResponseEntity.ok(primesOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // for admin to fetch all data which stored in DB
    @GetMapping("/generate-primenum/all")
    public ResponseEntity<List<PrimeNumberRecord>> getAllPrimeNumberRecords() {
        Optional<List<PrimeNumberRecord>> recordsOptional = service.getAllPrimeNumberRecords();
        if (recordsOptional.isPresent()) {
            return ResponseEntity.ok(recordsOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // for admin to fetch  data for particular id which stored in DB
    @GetMapping("/generate-primenum/{id}")
    public ResponseEntity<PrimeNumberRecord> getPrimeNumberRecordById(@PathVariable Long id) {
        Optional<PrimeNumberRecord> recordOptional = service.getPrimeNumberRecordById(id);
        if (recordOptional.isPresent()) {
            return ResponseEntity.ok(recordOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
