package com.studentanalytics.prediction.dto;

/**
 * Response DTO for score prediction
 */
public class ScorePredictionResponse {
    private Double predictedScore;
    private String timestamp;
    
    public ScorePredictionResponse() {
    }
    
    public ScorePredictionResponse(Double predictedScore) {
        this.predictedScore = Math.round(predictedScore * 100.0) / 100.0;
        this.timestamp = java.time.Instant.now().toString();
    }
    
    public ScorePredictionResponse(Double predictedScore, String timestamp) {
        this.predictedScore = predictedScore;
        this.timestamp = timestamp;
    }
    
    public Double getPredictedScore() {
        return predictedScore;
    }
    
    public void setPredictedScore(Double predictedScore) {
        this.predictedScore = predictedScore;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}