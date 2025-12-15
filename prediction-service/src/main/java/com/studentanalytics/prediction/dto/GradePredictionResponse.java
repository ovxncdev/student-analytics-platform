package com.studentanalytics.prediction.dto;

/**
 * Response DTO for grade prediction
 */
public class GradePredictionResponse {
    private String predictedGrade;
    private String timestamp;
    
    public GradePredictionResponse() {
    }
    
    public GradePredictionResponse(String predictedGrade) {
        this.predictedGrade = predictedGrade;
        this.timestamp = java.time.Instant.now().toString();
    }
    
    public GradePredictionResponse(String predictedGrade, String timestamp) {
        this.predictedGrade = predictedGrade;
        this.timestamp = timestamp;
    }
    
    public String getPredictedGrade() {
        return predictedGrade;
    }
    
    public void setPredictedGrade(String predictedGrade) {
        this.predictedGrade = predictedGrade;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}