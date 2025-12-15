package com.studentanalytics.prediction.dto;

/**
 * Request DTO for prediction endpoints
 * Contains student performance metrics
 */
public class PredictionRequest {
    
    private Double attendancePercentage;
    private Double quizAverage;
    private Double assignmentAverage;
    private Double midtermScore;
    private Integer participationScore;
    private Double studyHoursPerWeek;
    private Double previousGpa;
    
    // Default constructor
    public PredictionRequest() {
    }
    
    // All-args constructor
    public PredictionRequest(Double attendancePercentage, Double quizAverage, 
                           Double assignmentAverage, Double midtermScore,
                           Integer participationScore, Double studyHoursPerWeek, 
                           Double previousGpa) {
        this.attendancePercentage = attendancePercentage;
        this.quizAverage = quizAverage;
        this.assignmentAverage = assignmentAverage;
        this.midtermScore = midtermScore;
        this.participationScore = participationScore;
        this.studyHoursPerWeek = studyHoursPerWeek;
        this.previousGpa = previousGpa;
    }
    
    // Getters
    public Double getAttendancePercentage() {
        return attendancePercentage;
    }
    
    public Double getQuizAverage() {
        return quizAverage;
    }
    
    public Double getAssignmentAverage() {
        return assignmentAverage;
    }
    
    public Double getMidtermScore() {
        return midtermScore;
    }
    
    public Integer getParticipationScore() {
        return participationScore;
    }
    
    public Double getStudyHoursPerWeek() {
        return studyHoursPerWeek;
    }
    
    public Double getPreviousGpa() {
        return previousGpa;
    }
    
    // Setters
    public void setAttendancePercentage(Double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }
    
    public void setQuizAverage(Double quizAverage) {
        this.quizAverage = quizAverage;
    }
    
    public void setAssignmentAverage(Double assignmentAverage) {
        this.assignmentAverage = assignmentAverage;
    }
    
    public void setMidtermScore(Double midtermScore) {
        this.midtermScore = midtermScore;
    }
    
    public void setParticipationScore(Integer participationScore) {
        this.participationScore = participationScore;
    }
    
    public void setStudyHoursPerWeek(Double studyHoursPerWeek) {
        this.studyHoursPerWeek = studyHoursPerWeek;
    }
    
    public void setPreviousGpa(Double previousGpa) {
        this.previousGpa = previousGpa;
    }
    
    /**
     * Convert to feature array for model input
     */
    public double[] toFeatureArray() {
        return new double[]{
            attendancePercentage != null ? attendancePercentage : 0.0,
            quizAverage != null ? quizAverage : 0.0,
            assignmentAverage != null ? assignmentAverage : 0.0,
            midtermScore != null ? midtermScore : 0.0,
            participationScore != null ? participationScore.doubleValue() : 0.0,
            studyHoursPerWeek != null ? studyHoursPerWeek : 0.0,
            previousGpa != null ? previousGpa : 0.0
        };
    }
    
    /**
     * Validate input values
     */
    public boolean isValid() {
        return attendancePercentage != null && attendancePercentage >= 0 && attendancePercentage <= 100
            && quizAverage != null && quizAverage >= 0 && quizAverage <= 100
            && assignmentAverage != null && assignmentAverage >= 0 && assignmentAverage <= 100
            && midtermScore != null && midtermScore >= 0 && midtermScore <= 100
            && participationScore != null && participationScore >= 1 && participationScore <= 10
            && studyHoursPerWeek != null && studyHoursPerWeek >= 0 && studyHoursPerWeek <= 20
            && previousGpa != null && previousGpa >= 0.0 && previousGpa <= 4.0;
    }
}