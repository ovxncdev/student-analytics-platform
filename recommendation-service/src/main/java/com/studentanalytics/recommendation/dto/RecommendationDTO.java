package com.studentanalytics.recommendation.dto;

import java.util.List;

public class RecommendationDTO {

    private Long studentId;
    private Long courseId;
    private String riskLevel;
    private List<String> recommendations;
    private String priority;

    public RecommendationDTO() {
    }

    public RecommendationDTO(Long studentId, Long courseId, String riskLevel, List<String> recommendations, String priority) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.riskLevel = riskLevel;
        this.recommendations = recommendations;
        this.priority = priority;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}