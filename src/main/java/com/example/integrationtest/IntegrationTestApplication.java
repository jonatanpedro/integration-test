package com.example.integrationtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@Configuration
@IntegrationComponentScan
public class IntegrationTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegrationTestApplication.class, args);
	}
}
