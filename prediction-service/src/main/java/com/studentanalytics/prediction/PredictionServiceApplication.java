package com.studentanalytics.prediction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application for Prediction Service
 * 
 * This microservice provides REST API endpoints for predicting:
 * - Student letter grades (A, B, C, D, F)
 * - Numeric final scores (0-100)
 * - Risk assessment (Pass/Fail probability)
 * 
 * @author Student Analytics Team
 * @version 1.0.0
 */
@SpringBootApplication
public class PredictionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PredictionServiceApplication.class, args);
        
        // Print startup message
        String line = "============================================================";
        System.out.println(line);
        System.out.println("Prediction Service Started Successfully!");
        System.out.println("API available at: http://localhost:8080");
        System.out.println("Health check: http://localhost:8080/actuator/health");
        System.out.println(line);
    }
}