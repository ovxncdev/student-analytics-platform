package com.studentanalytics.performance.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PerformanceDTO {

    private Long performanceId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    private BigDecimal attendancePercentage;
    private BigDecimal quizAverage;
    private BigDecimal assignmentAverage;
    private BigDecimal midtermScore;
    private Integer participationScore;
    private Integer studyHoursPerWeek;
    private BigDecimal previousGpa;
    private BigDecimal finalScore;
    private String finalGrade;
    private Long updatedBy;

    public PerformanceDTO() {
    }

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
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

    public BigDecimal getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(BigDecimal attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public BigDecimal getQuizAverage() {
        return quizAverage;
    }

    public void setQuizAverage(BigDecimal quizAverage) {
        this.quizAverage = quizAverage;
    }

    public BigDecimal getAssignmentAverage() {
        return assignmentAverage;
    }

    public void setAssignmentAverage(BigDecimal assignmentAverage) {
        this.assignmentAverage = assignmentAverage;
    }

    public BigDecimal getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(BigDecimal midtermScore) {
        this.midtermScore = midtermScore;
    }

    public Integer getParticipationScore() {
        return participationScore;
    }

    public void setParticipationScore(Integer participationScore) {
        this.participationScore = participationScore;
    }

    public Integer getStudyHoursPerWeek() {
        return studyHoursPerWeek;
    }

    public void setStudyHoursPerWeek(Integer studyHoursPerWeek) {
        this.studyHoursPerWeek = studyHoursPerWeek;
    }

    public BigDecimal getPreviousGpa() {
        return previousGpa;
    }

    public void setPreviousGpa(BigDecimal previousGpa) {
        this.previousGpa = previousGpa;
    }

    public BigDecimal getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(BigDecimal finalScore) {
        this.finalScore = finalScore;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}