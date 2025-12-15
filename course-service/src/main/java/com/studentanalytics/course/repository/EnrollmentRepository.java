package com.studentanalytics.course.repository;

import com.studentanalytics.course.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByStudentIdAndEnrollmentStatus(Long studentId, String status);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.studentId = :studentId AND e.enrollmentStatus = 'active'")
    Long countActiveEnrollmentsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.courseId = :courseId AND e.enrollmentStatus = 'active'")
    Long countActiveEnrollmentsByCourseId(@Param("courseId") Long courseId);
}