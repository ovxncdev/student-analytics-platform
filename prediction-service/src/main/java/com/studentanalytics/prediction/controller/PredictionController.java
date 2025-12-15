package com.studentanalytics.prediction.controller;

import com.studentanalytics.prediction.dto.*;
import com.studentanalytics.prediction.util.ModelLoader;
import com.studentanalytics.prediction.util.ModelLoader.RiskPrediction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Student Performance Predictions
 * 
 * Provides endpoints for:
 * - Grade prediction (A, B, C, D, F)
 * - Score prediction (0-100)
 * - Risk assessment (Pass/Fail probability)
 * - Complete prediction (all above combined)
 */
@RestController
@RequestMapping("/api/predict")
@CrossOrigin(origins = "*")
public class PredictionController {

    @Autowired
    private ModelLoader modelLoader;
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "prediction-service");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get model information
     */
    @GetMapping("/model-info")
    public ResponseEntity<Map<String, Object>> getModelInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("features", modelLoader.getFeatures());
        info.put("gradeClasses", modelLoader.getGradeClasses());
        info.put("featureCount", modelLoader.getFeatures().size());
        return ResponseEntity.ok(info);
    }
    
    /**
     * Predict grade only
     * GET /api/predict/grade?attendancePercentage=85&quizAverage=78&...
     */
    @GetMapping("/grade")
    public ResponseEntity<?> predictGrade(
            @RequestParam Double attendancePercentage,
            @RequestParam Double quizAverage,
            @RequestParam Double assignmentAverage,
            @RequestParam Double midtermScore,
            @RequestParam Integer participationScore,
            @RequestParam Double studyHoursPerWeek,
            @RequestParam Double previousGpa) {
        
        try {
            double[] features = new double[]{
                attendancePercentage, quizAverage, assignmentAverage,
                midtermScore, participationScore.doubleValue(),
                studyHoursPerWeek, previousGpa
            };
            
            String predictedGrade = modelLoader.predictGrade(features);
            return ResponseEntity.ok(new GradePredictionResponse(predictedGrade));
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Predict score only
     * GET /api/predict/score?attendancePercentage=85&quizAverage=78&...
     */
    @GetMapping("/score")
    public ResponseEntity<?> predictScore(
            @RequestParam Double attendancePercentage,
            @RequestParam Double quizAverage,
            @RequestParam Double assignmentAverage,
            @RequestParam Double midtermScore,
            @RequestParam Integer participationScore,
            @RequestParam Double studyHoursPerWeek,
            @RequestParam Double previousGpa) {
        
        try {
            double[] features = new double[]{
                attendancePercentage, quizAverage, assignmentAverage,
                midtermScore, participationScore.doubleValue(),
                studyHoursPerWeek, previousGpa
            };
            
            double predictedScore = modelLoader.predictScore(features);
            return ResponseEntity.ok(new ScorePredictionResponse(predictedScore));
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Assess risk (Pass/Fail prediction)
     * GET /api/predict/risk?attendancePercentage=85&quizAverage=78&...
     */
    @GetMapping("/risk")
    public ResponseEntity<?> assessRisk(
            @RequestParam Double attendancePercentage,
            @RequestParam Double quizAverage,
            @RequestParam Double assignmentAverage,
            @RequestParam Double midtermScore,
            @RequestParam Integer participationScore,
            @RequestParam Double studyHoursPerWeek,
            @RequestParam Double previousGpa) {
        
        try {
            double[] features = new double[]{
                attendancePercentage, quizAverage, assignmentAverage,
                midtermScore, participationScore.doubleValue(),
                studyHoursPerWeek, previousGpa
            };
            
            RiskPrediction risk = modelLoader.assessRisk(features);
            
            Map<String, Object> response = new HashMap<>();
            response.put("prediction", risk.prediction);
            response.put("riskLevel", risk.riskLevel);
            response.put("confidence", Math.round(risk.confidence * 10000.0) / 10000.0);
            response.put("passProbability", Math.round(risk.passProbability * 10000.0) / 10000.0);
            response.put("timestamp", java.time.Instant.now().toString());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Complete prediction (all models)
     * POST /api/predict/complete
     * Body: PredictionRequest JSON
     */
    @PostMapping("/complete")
    public ResponseEntity<?> completePredict(@RequestBody PredictionRequest request) {
        try {
            // Validate input
            if (!request.isValid()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid input values");
                return ResponseEntity.badRequest().body(error);
            }
            
            double[] features = request.toFeatureArray();
            
            // Get all predictions
            String grade = modelLoader.predictGrade(features);
            double score = modelLoader.predictScore(features);
            RiskPrediction risk = modelLoader.assessRisk(features);
            
            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("predictedGrade", grade);
            response.put("predictedScore", Math.round(score * 100.0) / 100.0);
            response.put("riskPrediction", risk.prediction);
            response.put("riskLevel", risk.riskLevel);
            response.put("confidence", Math.round(risk.confidence * 10000.0) / 10000.0);
            response.put("passProbability", Math.round(risk.passProbability * 10000.0) / 10000.0);
            response.put("timestamp", java.time.Instant.now().toString());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Batch prediction
     * POST /api/predict/batch
     * Body: Array of PredictionRequest
     */
    @PostMapping("/batch")
    public ResponseEntity<?> batchPredict(@RequestBody PredictionRequest[] requests) {
        try {
            Map<String, Object>[] results = new HashMap[requests.length];
            
            for (int i = 0; i < requests.length; i++) {
                PredictionRequest request = requests[i];
                
                if (!request.isValid()) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "Invalid input at index " + i);
                    results[i] = error;
                    continue;
                }
                
                double[] features = request.toFeatureArray();
                
                String grade = modelLoader.predictGrade(features);
                double score = modelLoader.predictScore(features);
                RiskPrediction risk = modelLoader.assessRisk(features);
                
                results[i] = new HashMap<>();
                results[i].put("index", i);
                results[i].put("predictedGrade", grade);
                results[i].put("predictedScore", Math.round(score * 100.0) / 100.0);
                results[i].put("riskLevel", risk.riskLevel);
            }
            
            Map<String, Object> finalResponse = new HashMap<>();
            finalResponse.put("results", results);
            finalResponse.put("count", requests.length);
            
            return ResponseEntity.ok(finalResponse);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}