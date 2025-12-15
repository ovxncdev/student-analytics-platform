package com.studentanalytics.performance.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "performance")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_id")
    private Long performanceId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "attendance_percentage", precision = 5, scale = 2)
    private BigDecimal attendancePercentage;

    @Column(name = "quiz_average", precision = 5, scale = 2)
    private BigDecimal quizAverage;

    @Column(name = "assignment_average", precision = 5, scale = 2)
    private BigDecimal assignmentAverage;

    @Column(name = "midterm_score", precision = 5, scale = 2)
    private BigDecimal midtermScore;

    @Column(name = "participation_score")
    private Integer participationScore;

    @Column(name = "study_hours_per_week")
    private Integer studyHoursPerWeek;

    @Column(name = "previous_gpa", precision = 3, scale = 2)
    private BigDecimal previousGpa;

    @Column(name = "final_score", precision = 5, scale = 2)
    private BigDecimal finalScore;

    @Column(name = "final_grade", length = 2)
    private String finalGrade;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    public Performance() {
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    // Getters and Setters
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}