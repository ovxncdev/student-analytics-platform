package com.studentanalytics.analytics.dto;

import java.math.BigDecimal;
import java.util.Map;

public class AnalyticsDTO {

    private Long totalStudents;
    private Long totalCourses;
    private Long totalEnrollments;
    private BigDecimal averageAttendance;
    private BigDecimal averageGrade;
    private Map<String, Long> riskDistribution;
    private Map<String, Long> gradeDistribution;

    public AnalyticsDTO() {
    }

    public Long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Long getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(Long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public Long getTotalEnrollments() {
        return totalEnrollments;
    }

    public void setTotalEnrollments(Long totalEnrollments) {
        this.totalEnrollments = totalEnrollments;
    }

    public BigDecimal getAverageAttendance() {
        return averageAttendance;
    }

    public void setAverageAttendance(BigDecimal averageAttendance) {
        this.averageAttendance = averageAttendance;
    }

    public BigDecimal getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(BigDecimal averageGrade) {
        this.averageGrade = averageGrade;
    }

    public Map<String, Long> getRiskDistribution() {
        return riskDistribution;
    }

    public void setRiskDistribution(Map<String, Long> riskDistribution) {
        this.riskDistribution = riskDistribution;
    }

    public Map<String, Long> getGradeDistribution() {
        return gradeDistribution;
    }

    public void setGradeDistribution(Map<String, Long> gradeDistribution) {
        this.gradeDistribution = gradeDistribution;
    }
}