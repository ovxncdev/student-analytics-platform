package com.studentanalytics.analytics.service;

import com.studentanalytics.analytics.dto.AnalyticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public AnalyticsDTO getOverallAnalytics() {
        AnalyticsDTO analytics = new AnalyticsDTO();

        analytics.setTotalStudents(getTotalStudents());
        analytics.setTotalCourses(getTotalCourses());
        analytics.setTotalEnrollments(getTotalEnrollments());
        analytics.setAverageAttendance(getAverageAttendance());
        analytics.setAverageGrade(getAverageGrade());
        analytics.setRiskDistribution(getRiskDistribution());
        analytics.setGradeDistribution(getGradeDistribution());

        return analytics;
    }

    private Long getTotalStudents() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM students", Long.class);
    }

    private Long getTotalCourses() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM courses WHERE is_active = TRUE", Long.class);
    }

    private Long getTotalEnrollments() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM enrollments WHERE enrollment_status = 'active'", Long.class);
    }

    private BigDecimal getAverageAttendance() {
        return jdbcTemplate.queryForObject("SELECT AVG(attendance_percentage) FROM performance", BigDecimal.class);
    }

    private BigDecimal getAverageGrade() {
        return jdbcTemplate.queryForObject("SELECT AVG(midterm_score) FROM performance", BigDecimal.class);
    }

    private Map<String, Long> getRiskDistribution() {
        Map<String, Long> distribution = new HashMap<String, Long>();
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(
            "SELECT risk_level, COUNT(*) as count FROM predictions GROUP BY risk_level"
        );

        for (Map<String, Object> row : results) {
            String riskLevel = (String) row.get("risk_level");
            Long count = ((Number) row.get("count")).longValue();
            distribution.put(riskLevel, count);
        }

        return distribution;
    }

    private Map<String, Long> getGradeDistribution() {
        Map<String, Long> distribution = new HashMap<String, Long>();
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(
            "SELECT predicted_grade, COUNT(*) as count FROM predictions GROUP BY predicted_grade"
        );

        for (Map<String, Object> row : results) {
            String grade = (String) row.get("predicted_grade");
            Long count = ((Number) row.get("count")).longValue();
            distribution.put(grade, count);
        }

        return distribution;
    }

    public Map<String, Object> getCourseAnalytics(Long courseId) {
        Map<String, Object> analytics = new HashMap<String, Object>();

        Long enrolledStudents = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM enrollments WHERE course_id = ? AND enrollment_status = 'active'",
            Long.class, courseId
        );

        BigDecimal avgAttendance = jdbcTemplate.queryForObject(
            "SELECT AVG(attendance_percentage) FROM performance WHERE course_id = ?",
            BigDecimal.class, courseId
        );

        BigDecimal avgScore = jdbcTemplate.queryForObject(
            "SELECT AVG(midterm_score) FROM performance WHERE course_id = ?",
            BigDecimal.class, courseId
        );

        analytics.put("enrolledStudents", enrolledStudents);
        analytics.put("averageAttendance", avgAttendance);
        analytics.put("averageScore", avgScore);

        return analytics;
    }

    public Map<String, Object> getStudentAnalytics(Long studentId) {
        Map<String, Object> analytics = new HashMap<String, Object>();

        Long totalCourses = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND enrollment_status = 'active'",
            Long.class, studentId
        );

        BigDecimal avgAttendance = jdbcTemplate.queryForObject(
            "SELECT AVG(attendance_percentage) FROM performance WHERE student_id = ?",
            BigDecimal.class, studentId
        );

        BigDecimal avgScore = jdbcTemplate.queryForObject(
            "SELECT AVG(midterm_score) FROM performance WHERE student_id = ?",
            BigDecimal.class, studentId
        );

        analytics.put("totalCourses", totalCourses);
        analytics.put("averageAttendance", avgAttendance);
        analytics.put("averageScore", avgScore);

        return analytics;
    }
}