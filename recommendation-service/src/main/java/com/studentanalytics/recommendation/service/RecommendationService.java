package com.studentanalytics.recommendation.service;

import com.studentanalytics.recommendation.dto.RecommendationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<RecommendationDTO> getRecommendationsForStudent(Long studentId) {
        List<RecommendationDTO> recommendations = new ArrayList<RecommendationDTO>();

        String sql = "SELECT p.student_id, p.course_id, pred.risk_level, " +
                     "p.attendance_percentage, p.quiz_average, p.assignment_average, " +
                     "p.midterm_score, p.participation_score, p.study_hours_per_week " +
                     "FROM performance p " +
                     "JOIN predictions pred ON p.student_id = pred.student_id AND p.course_id = pred.course_id " +
                     "WHERE p.student_id = ?";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, studentId);

        for (Map<String, Object> row : results) {
            Long courseId = ((Number) row.get("course_id")).longValue();
            String riskLevel = (String) row.get("risk_level");
            BigDecimal attendance = (BigDecimal) row.get("attendance_percentage");
            BigDecimal quizAvg = (BigDecimal) row.get("quiz_average");
            BigDecimal assignmentAvg = (BigDecimal) row.get("assignment_average");
            BigDecimal midterm = (BigDecimal) row.get("midterm_score");
            Integer participation = (Integer) row.get("participation_score");
            Integer studyHours = (Integer) row.get("study_hours_per_week");

            List<String> recs = generateRecommendations(attendance, quizAvg, assignmentAvg, 
                                                         midterm, participation, studyHours, riskLevel);
            String priority = determinePriority(riskLevel);

            recommendations.add(new RecommendationDTO(studentId, courseId, riskLevel, recs, priority));
        }

        return recommendations;
    }

    public RecommendationDTO getRecommendationsForStudentCourse(Long studentId, Long courseId) {
        String sql = "SELECT p.student_id, p.course_id, pred.risk_level, " +
                     "p.attendance_percentage, p.quiz_average, p.assignment_average, " +
                     "p.midterm_score, p.participation_score, p.study_hours_per_week " +
                     "FROM performance p " +
                     "JOIN predictions pred ON p.student_id = pred.student_id AND p.course_id = pred.course_id " +
                     "WHERE p.student_id = ? AND p.course_id = ?";

        Map<String, Object> row = jdbcTemplate.queryForMap(sql, studentId, courseId);

        String riskLevel = (String) row.get("risk_level");
        BigDecimal attendance = (BigDecimal) row.get("attendance_percentage");
        BigDecimal quizAvg = (BigDecimal) row.get("quiz_average");
        BigDecimal assignmentAvg = (BigDecimal) row.get("assignment_average");
        BigDecimal midterm = (BigDecimal) row.get("midterm_score");
        Integer participation = (Integer) row.get("participation_score");
        Integer studyHours = (Integer) row.get("study_hours_per_week");

        List<String> recs = generateRecommendations(attendance, quizAvg, assignmentAvg, 
                                                     midterm, participation, studyHours, riskLevel);
        String priority = determinePriority(riskLevel);

        return new RecommendationDTO(studentId, courseId, riskLevel, recs, priority);
    }

    private List<String> generateRecommendations(BigDecimal attendance, BigDecimal quizAvg, 
                                                  BigDecimal assignmentAvg, BigDecimal midterm,
                                                  Integer participation, Integer studyHours, String riskLevel) {
        List<String> recommendations = new ArrayList<String>();

        if (attendance.compareTo(new BigDecimal("80")) < 0) {
            recommendations.add("Improve attendance - current: " + attendance + "%");
        }

        if (quizAvg.compareTo(new BigDecimal("70")) < 0) {
            recommendations.add("Focus on quiz preparation - current average: " + quizAvg);
        }

        if (assignmentAvg.compareTo(new BigDecimal("70")) < 0) {
            recommendations.add("Complete assignments on time - current average: " + assignmentAvg);
        }

        if (midterm.compareTo(new BigDecimal("70")) < 0) {
            recommendations.add("Seek additional tutoring - midterm score: " + midterm);
        }

        if (participation < 5) {
            recommendations.add("Increase class participation - current score: " + participation + "/10");
        }

        if (studyHours < 10) {
            recommendations.add("Increase study time - currently studying " + studyHours + " hours/week");
        }

        if ("High".equals(riskLevel)) {
            recommendations.add("URGENT: Schedule meeting with academic advisor");
            recommendations.add("Consider enrolling in tutoring program");
        } else if ("Medium".equals(riskLevel)) {
            recommendations.add("Meet with instructor during office hours");
            recommendations.add("Form or join a study group");
        } else {
            recommendations.add("Maintain current study habits");
            recommendations.add("Continue attending classes regularly");
        }

        return recommendations;
    }

    private String determinePriority(String riskLevel) {
        if ("High".equals(riskLevel)) {
            return "URGENT";
        } else if ("Medium".equals(riskLevel)) {
            return "MODERATE";
        } else {
            return "LOW";
        }
    }
}