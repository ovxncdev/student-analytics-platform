package com.studentanalytics.course.service;

import com.studentanalytics.course.dto.CourseDTO;
import com.studentanalytics.course.entity.Course;
import com.studentanalytics.course.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public List<Course> getCoursesByTeacherId(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public List<Course> getCoursesBySemester(String semester, Integer year) {
        return courseRepository.findBySemesterAndYear(semester, year);
    }

    public List<Course> getActiveCourses() {
        return courseRepository.findByIsActive(true);
    }

    @Transactional
    public Course createCourse(CourseDTO courseDTO) {
        if (courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
            throw new RuntimeException("Course code already exists");
        }

        Course course = new Course();
        course.setCourseCode(courseDTO.getCourseCode());
        course.setCourseName(courseDTO.getCourseName());
        course.setTeacherId(courseDTO.getTeacherId());
        course.setSemester(courseDTO.getSemester());
        course.setYear(courseDTO.getYear());
        course.setMaxStudents(courseDTO.getMaxStudents() != null ? courseDTO.getMaxStudents() : 50);
        course.setCredits(courseDTO.getCredits() != null ? courseDTO.getCredits() : 3);
        course.setDescription(courseDTO.getDescription());
        course.setIsActive(true);

        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(Long courseId, CourseDTO courseDTO) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (!courseOptional.isPresent()) {
            throw new RuntimeException("Course not found");
        }

        Course course = courseOptional.get();

        if (courseDTO.getCourseName() != null) {
            course.setCourseName(courseDTO.getCourseName());
        }
        if (courseDTO.getTeacherId() != null) {
            course.setTeacherId(courseDTO.getTeacherId());
        }
        if (courseDTO.getSemester() != null) {
            course.setSemester(courseDTO.getSemester());
        }
        if (courseDTO.getYear() != null) {
            course.setYear(courseDTO.getYear());
        }
        if (courseDTO.getMaxStudents() != null) {
            course.setMaxStudents(courseDTO.getMaxStudents());
        }
        if (courseDTO.getCredits() != null) {
            course.setCredits(courseDTO.getCredits());
        }
        if (courseDTO.getDescription() != null) {
            course.setDescription(courseDTO.getDescription());
        }
        if (courseDTO.getIsActive() != null) {
            course.setIsActive(courseDTO.getIsActive());
        }

        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(courseId);
    }
}