package com.studentanalytics.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("admin123 hash: " + encoder.encode("admin123"));
        System.out.println("teacher123 hash: " + encoder.encode("teacher123"));
        System.out.println("student123 hash: " + encoder.encode("student123"));
    }
}