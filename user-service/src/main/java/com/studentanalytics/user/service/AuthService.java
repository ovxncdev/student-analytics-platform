package com.studentanalytics.user.service;

import com.studentanalytics.user.dto.LoginRequest;
import com.studentanalytics.user.dto.LoginResponse;
import com.studentanalytics.user.entity.User;
import com.studentanalytics.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (!userOptional.isPresent()) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userOptional.get();

        if (!user.getIsActive()) {
            throw new RuntimeException("User account is inactive");
        }

        if (!loginRequest.getPassword().equals(user.getPasswordHash())) {
    throw new RuntimeException("Invalid username or password");
}

        String token = jwtService.generateToken(user.getUsername(), user.getUserId(), user.getRole());

        return new LoginResponse(token, user.getUserId(), user.getUsername(), user.getRole(), user.getFullName());
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            return !jwtService.isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}