package com.studentanalytics.user.service;

import com.studentanalytics.user.dto.UserDTO;
import com.studentanalytics.user.entity.User;
import com.studentanalytics.user.entity.Teacher;
import com.studentanalytics.user.entity.Student;
import com.studentanalytics.user.repository.UserRepository;
import com.studentanalytics.user.repository.TeacherRepository;
import com.studentanalytics.user.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> getUsersByRole(Integer role) {
        return userRepository.findByRole(role);
    }

    @Transactional
    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPasswordHash(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setFullName(userDTO.getFullName());
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        if (userDTO.getRole() == 2) {
            Teacher teacher = new Teacher();
            teacher.setUserId(savedUser.getUserId());
            teacher.setEmployeeId(userDTO.getEmployeeId());
            teacher.setDepartment(userDTO.getDepartment());
            teacher.setOfficeLocation(userDTO.getOfficeLocation());
            teacher.setPhone(userDTO.getPhone());
            teacherRepository.save(teacher);
        } else if (userDTO.getRole() == 3) {
            Student student = new Student();
            student.setUserId(savedUser.getUserId());
            student.setStudentNumber(userDTO.getStudentNumber());
            student.setMajor(userDTO.getMajor());
            student.setYear(userDTO.getYear());
            student.setSemester(userDTO.getSemester());
            student.setMaxCoursesPerSemester(userDTO.getMaxCoursesPerSemester() != null ? userDTO.getMaxCoursesPerSemester() : 3);
            studentRepository.save(student);
        }

        return savedUser;
    }

    @Transactional
    public User updateUser(Long userId, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPasswordHash(userDTO.getPassword());
        }

        if (userDTO.getFullName() != null) {
            user.setFullName(userDTO.getFullName());
        }

        if (userDTO.getIsActive() != null) {
            user.setIsActive(userDTO.getIsActive());
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}