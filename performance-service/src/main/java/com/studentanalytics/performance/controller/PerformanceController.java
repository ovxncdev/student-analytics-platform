package com.studentanalytics.performance.controller;

import com.studentanalytics.performance.dto.PerformanceDTO;
import com.studentanalytics.performance.entity.Performance;
import com.studentanalytics.performance.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/performance")
@CrossOrigin(origins = "*")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @GetMapping
    public ResponseEntity<List<Performance>> getAllPerformance() {
        List<Performance> performances = performanceService.getAllPerformance();
        return ResponseEntity.ok(performances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPerformanceById(@PathVariable Long id) {
        try {
            Performance performance = performanceService.getPerformanceById(id)
                    .orElseThrow(() -> new RuntimeException("Performance record not found"));
            return ResponseEntity.ok(performance);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Performance>> getPerformanceByStudent(@PathVariable Long studentId) {
        List<Performance> performances = performanceService.getPerformanceByStudentId(studentId);
        return ResponseEntity.ok(performances);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Performance>> getPerformanceByCourse(@PathVariable Long courseId) {
        List<Performance> performances = performanceService.getPerformanceByCourseId(courseId);
        return ResponseEntity.ok(performances);
    }

    @PostMapping
    public ResponseEntity<?> createPerformance(@Valid @RequestBody PerformanceDTO performanceDTO) {
        try {
            Performance performance = performanceService.createPerformance(performanceDTO);
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("message", "Performance record created successfully");
            response.put("performanceId", performance.getPerformanceId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerformance(@PathVariable Long id, @Valid @RequestBody PerformanceDTO performanceDTO) {
        try {
            Performance performance = performanceService.updatePerformance(id, performanceDTO);
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("message", "Performance record updated successfully");
            response.put("performance", performance);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerformance(@PathVariable Long id) {
        try {
            performanceService.deletePerformance(id);
            Map<String, String> response = new HashMap<String, String>();
            response.put("message", "Performance record deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}