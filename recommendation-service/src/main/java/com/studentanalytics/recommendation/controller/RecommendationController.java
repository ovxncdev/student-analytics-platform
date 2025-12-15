package com.studentanalytics.recommendation.controller;

import com.studentanalytics.recommendation.dto.RecommendationDTO;
import com.studentanalytics.recommendation.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getRecommendationsForStudent(@PathVariable Long studentId) {
        try {
            List<RecommendationDTO> recommendations = recommendationService.getRecommendationsForStudent(studentId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("message", "Error generating recommendations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<?> getRecommendationsForStudentCourse(
            @PathVariable Long studentId, 
            @PathVariable Long courseId) {
        try {
            RecommendationDTO recommendation = recommendationService.getRecommendationsForStudentCourse(studentId, courseId);
            return ResponseEntity.ok(recommendation);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("message", "Error generating recommendation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}