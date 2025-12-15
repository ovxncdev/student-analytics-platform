package com.studentanalytics.analytics.controller;

import com.studentanalytics.analytics.dto.AnalyticsDTO;
import com.studentanalytics.analytics.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/overview")
    public ResponseEntity<AnalyticsDTO> getOverallAnalytics() {
        AnalyticsDTO analytics = analyticsService.getOverallAnalytics();
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Map<String, Object>> getCourseAnalytics(@PathVariable Long courseId) {
        Map<String, Object> analytics = analyticsService.getCourseAnalytics(courseId);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentAnalytics(@PathVariable Long studentId) {
        Map<String, Object> analytics = analyticsService.getStudentAnalytics(studentId);
        return ResponseEntity.ok(analytics);
    }
}