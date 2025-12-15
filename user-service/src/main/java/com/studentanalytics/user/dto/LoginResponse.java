package com.studentanalytics.user.dto;

public class LoginResponse {

    private String token;
    private Long userId;
    private String username;
    private Integer role;
    private String fullName;
    private String message;

    // Constructors
    public LoginResponse() {
    }

    public LoginResponse(String token, Long userId, String username, Integer role, String fullName) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.fullName = fullName;
        this.message = "Login successful";
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}