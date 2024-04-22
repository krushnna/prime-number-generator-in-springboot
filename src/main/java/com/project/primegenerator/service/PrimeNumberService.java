package com.project.primegenerator.service;

import com.project.primegenerator.model.PrimeNumberRecord;
import com.project.primegenerator.repository.PrimeNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PrimeNumberService {

    @Autowired
    private PrimeNumberRepository repository;

    public Optional<List<Integer>> generatePrimes(int start, int end, String algorithm) {
        List<Integer> primes ;

        try {
            switch (algorithm) {
                case "SieveOfEratosthenes":
                    primes = sieveOfEratosthenes(start, end).orElseThrow(() -> new RuntimeException("Failed to generate primes using Sieve of Eratosthenes algorithm"));
                    break;
                case "SieveOfAtkin":
                    primes = sieveOfAtkin(start, end).orElseThrow(() -> new RuntimeException("Failed to generate primes using Sieve of Atkin algorithm"));
                    break;
                case "TrialDivision" :
                    primes = trialDivision(start, end).orElseThrow(() -> new RuntimeException("Failedto generate primes using default algorithm which is trial division"));
                    break;
                default:
                    System.out.println("Invalid algorithm: " + algorithm);
                    return Optional.empty();
            }
            // To save all entries in DB before returning
            repository.saveAll(createPrimeNumberRecords(start, end, primes.size(), algorithm));
            return Optional.of(primes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate primes", e);
        }
    }

    public Optional<List<Integer>> trialDivision(int start, int end) {
        List<Integer> primes = new ArrayList<>();

        try {
            for (int num = start; num <= end; num++) {
                if (isPrime(num)) {
                    primes.add(num);
                }
            }
            return Optional.of(primes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate primes", e);
        }
    }

    public Optional<List<PrimeNumberRecord>> getAllPrimeNumberRecords() {
        try {
            List<PrimeNumberRecord> records = repository.findAll();
            return Optional.of(records);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve prime number records", e);
        }
    }

    public Optional<PrimeNumberRecord> getPrimeNumberRecordById(Long id) {
        try {
            Optional<PrimeNumberRecord> recordOptional = repository.findById(id);
            return recordOptional;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve prime number record by ID", e);
        }
    }

    private boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Transactional //using transactional for atomicity
    protected List<PrimeNumberRecord> createPrimeNumberRecords(int start, int end, int numberOfPrimes, String algorithm) {
        Map<String,String> mapTimeComplexity = new HashMap<>();
        // its setting complexity based on alogrith, to store it in DB
        mapTimeComplexity.put("TrialDivision","O(sqrt(n))");
        mapTimeComplexity.put("SieveOfAtkin","O(n/log(log(n)))");
        mapTimeComplexity.put("SieveOfEratosthenes","O(n*log(log(n)))");

        List<PrimeNumberRecord> records = new ArrayList<>();
        PrimeNumberRecord record = new PrimeNumberRecord();
        record.setStartRange(start);
        record.setEndRange(end);
        record.setNumberOfPrimes(numberOfPrimes);
        record.setAlgorithm(algorithm);
        record.setTimestamp(LocalDateTime.now());
        record.setTimeComplexity(mapTimeComplexity.get(algorithm));
        records.add(record);
        return records;
    }

    public Optional<List<Integer>> sieveOfEratosthenes(int lower, int upper) {
        if (lower <= 1) {
            lower = 2;
        }

        int maxPrime = (int) Math.sqrt(upper);
        boolean[] isPrime = new boolean[upper + 1];
        Arrays.fill(isPrime, true);

        isPrime[0] = isPrime[1] = false;

        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= maxPrime; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= upper; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        for (int i = lower; i <= upper; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }

        return primes.isEmpty() ? Optional.empty() : Optional.of(primes);
    }



    public Optional<List<Integer>> sieveOfAtkin(int start, int end) {
        List<Integer> primes = new ArrayList<>();
        boolean[] isPrime = new boolean[end + 1];
        for (int x = 1; x * x <= end; x++) {
            for (int y = 1; y * y <= end; y++) {
                int n = (4 * x * x) + (y * y);
                if (n <= end && (n % 12 == 1 || n % 12 == 5)) {
                    isPrime[n] = !isPrime[n];
                }
                n = (3 * x * x) + (y * y);
                if (n <= end && n % 12 == 7) {
                    isPrime[n] = !isPrime[n];
                }
                n = (3 * x * x) - (y * y);
                if (x > y && n <= end && n % 12 == 11) {
                    isPrime[n] = !isPrime[n];
                }
            }
        }
        for (int i = 5; i * i <= end; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= end; j += i * i) {
                    isPrime[j] = false;
                }
            }
        }
        for (int i = start; i <= end; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }
        return primes.isEmpty() ? Optional.empty() : Optional.of(primes);
    }
}
