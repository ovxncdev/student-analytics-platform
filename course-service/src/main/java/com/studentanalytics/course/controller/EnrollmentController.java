package com.studentanalytics.course.controller;

import com.studentanalytics.course.dto.EnrollmentDTO;
import com.studentanalytics.course.entity.Enrollment;
import com.studentanalytics.course.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable Long id) {
        try {
            Enrollment enrollment = enrollmentService.getEnrollmentById(id)
                    .orElseThrow(() -> new RuntimeException("Enrollment not found"));
            return ResponseEntity.ok(enrollment);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/student/{studentId}/active")
    public ResponseEntity<List<Enrollment>> getActiveEnrollmentsByStudent(@PathVariable Long studentId) {
        List<Enrollment> enrollments = enrollmentService.getActiveEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping
    public ResponseEntity<?> createEnrollment(@Valid @RequestBody EnrollmentDTO enrollmentDTO) {
        try {
            Enrollment enrollment = enrollmentService.createEnrollment(enrollmentDTO);
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("message", "Enrollment created successfully");
            response.put("enrollmentId", enrollment.getEnrollmentId());
            response.put("status", enrollment.getEnrollmentStatus());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateEnrollmentStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            Enrollment enrollment = enrollmentService.updateEnrollmentStatus(id, status);
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("message", "Enrollment status updated successfully");
            response.put("enrollment", enrollment);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable Long id) {
        try {
            enrollmentService.deleteEnrollment(id);
            Map<String, String> response = new HashMap<String, String>();
            response.put("message", "Enrollment deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}