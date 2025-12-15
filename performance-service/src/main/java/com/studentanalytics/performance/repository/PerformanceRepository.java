package com.studentanalytics.performance.repository;

import com.studentanalytics.performance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    List<Performance> findByStudentId(Long studentId);

    List<Performance> findByCourseId(Long courseId);

    Optional<Performance> findByStudentIdAndCourseId(Long studentId, Long courseId);
}