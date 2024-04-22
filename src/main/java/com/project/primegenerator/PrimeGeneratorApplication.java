package com.project.primegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PrimeGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrimeGeneratorApplication.class, args);
	}

}
