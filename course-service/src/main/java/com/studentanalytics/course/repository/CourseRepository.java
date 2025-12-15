package com.studentanalytics.course.repository;

import com.studentanalytics.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findByTeacherId(Long teacherId);

    List<Course> findBySemesterAndYear(String semester, Integer year);

    List<Course> findByIsActive(Boolean isActive);

    boolean existsByCourseCode(String courseCode);
}