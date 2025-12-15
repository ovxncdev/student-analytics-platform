package com.studentanalytics.course.service;

import com.studentanalytics.course.dto.EnrollmentDTO;
import com.studentanalytics.course.entity.Course;
import com.studentanalytics.course.entity.Enrollment;
import com.studentanalytics.course.repository.CourseRepository;
import com.studentanalytics.course.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Optional<Enrollment> getEnrollmentById(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId);
    }

    public List<Enrollment> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public List<Enrollment> getActiveEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentIdAndEnrollmentStatus(studentId, "active");
    }

    @Transactional
    public Enrollment createEnrollment(EnrollmentDTO enrollmentDTO) {
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByStudentIdAndCourseId(
                enrollmentDTO.getStudentId(), enrollmentDTO.getCourseId());

        if (existingEnrollment.isPresent()) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        Long activeEnrollments = enrollmentRepository.countActiveEnrollmentsByStudentId(enrollmentDTO.getStudentId());
        if (activeEnrollments >= 3) {
            throw new RuntimeException("Student cannot enroll in more than 3 courses per semester");
        }

        Optional<Course> courseOptional = courseRepository.findById(enrollmentDTO.getCourseId());
        if (!courseOptional.isPresent()) {
            throw new RuntimeException("Course not found");
        }

        Course course = courseOptional.get();
        Long courseEnrollments = enrollmentRepository.countActiveEnrollmentsByCourseId(enrollmentDTO.getCourseId());
        if (courseEnrollments >= course.getMaxStudents()) {
            throw new RuntimeException("Course is full");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(enrollmentDTO.getStudentId());
        enrollment.setCourseId(enrollmentDTO.getCourseId());
        enrollment.setEnrollmentStatus("active");

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Enrollment updateEnrollmentStatus(Long enrollmentId, String status) {
        Optional<Enrollment> enrollmentOptional = enrollmentRepository.findById(enrollmentId);

        if (!enrollmentOptional.isPresent()) {
            throw new RuntimeException("Enrollment not found");
        }

        Enrollment enrollment = enrollmentOptional.get();
        enrollment.setEnrollmentStatus(status);

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void deleteEnrollment(Long enrollmentId) {
        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new RuntimeException("Enrollment not found");
        }
        enrollmentRepository.deleteById(enrollmentId);
    }
}