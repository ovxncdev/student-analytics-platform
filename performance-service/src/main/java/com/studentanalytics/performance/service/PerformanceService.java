package com.studentanalytics.performance.service;

import com.studentanalytics.performance.dto.PerformanceDTO;
import com.studentanalytics.performance.entity.Performance;
import com.studentanalytics.performance.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PerformanceService {

    @Autowired
    private PerformanceRepository performanceRepository;

    public List<Performance> getAllPerformance() {
        return performanceRepository.findAll();
    }

    public Optional<Performance> getPerformanceById(Long performanceId) {
        return performanceRepository.findById(performanceId);
    }

    public List<Performance> getPerformanceByStudentId(Long studentId) {
        return performanceRepository.findByStudentId(studentId);
    }

    public List<Performance> getPerformanceByCourseId(Long courseId) {
        return performanceRepository.findByCourseId(courseId);
    }

    @Transactional
    public Performance createPerformance(PerformanceDTO performanceDTO) {
        Optional<Performance> existing = performanceRepository.findByStudentIdAndCourseId(
                performanceDTO.getStudentId(), performanceDTO.getCourseId());

        if (existing.isPresent()) {
            throw new RuntimeException("Performance record already exists for this student and course");
        }

        Performance performance = new Performance();
        performance.setStudentId(performanceDTO.getStudentId());
        performance.setCourseId(performanceDTO.getCourseId());
        performance.setAttendancePercentage(performanceDTO.getAttendancePercentage());
        performance.setQuizAverage(performanceDTO.getQuizAverage());
        performance.setAssignmentAverage(performanceDTO.getAssignmentAverage());
        performance.setMidtermScore(performanceDTO.getMidtermScore());
        performance.setParticipationScore(performanceDTO.getParticipationScore());
        performance.setStudyHoursPerWeek(performanceDTO.getStudyHoursPerWeek());
        performance.setPreviousGpa(performanceDTO.getPreviousGpa());
        performance.setFinalScore(performanceDTO.getFinalScore());
        performance.setFinalGrade(performanceDTO.getFinalGrade());
        performance.setUpdatedBy(performanceDTO.getUpdatedBy());

        return performanceRepository.save(performance);
    }

    @Transactional
    public Performance updatePerformance(Long performanceId, PerformanceDTO performanceDTO) {
        Optional<Performance> performanceOptional = performanceRepository.findById(performanceId);

        if (!performanceOptional.isPresent()) {
            throw new RuntimeException("Performance record not found");
        }

        Performance performance = performanceOptional.get();

        if (performanceDTO.getAttendancePercentage() != null) {
            performance.setAttendancePercentage(performanceDTO.getAttendancePercentage());
        }
        if (performanceDTO.getQuizAverage() != null) {
            performance.setQuizAverage(performanceDTO.getQuizAverage());
        }
        if (performanceDTO.getAssignmentAverage() != null) {
            performance.setAssignmentAverage(performanceDTO.getAssignmentAverage());
        }
        if (performanceDTO.getMidtermScore() != null) {
            performance.setMidtermScore(performanceDTO.getMidtermScore());
        }
        if (performanceDTO.getParticipationScore() != null) {
            performance.setParticipationScore(performanceDTO.getParticipationScore());
        }
        if (performanceDTO.getStudyHoursPerWeek() != null) {
            performance.setStudyHoursPerWeek(performanceDTO.getStudyHoursPerWeek());
        }
        if (performanceDTO.getPreviousGpa() != null) {
            performance.setPreviousGpa(performanceDTO.getPreviousGpa());
        }
        if (performanceDTO.getFinalScore() != null) {
            performance.setFinalScore(performanceDTO.getFinalScore());
        }
        if (performanceDTO.getFinalGrade() != null) {
            performance.setFinalGrade(performanceDTO.getFinalGrade());
        }
        if (performanceDTO.getUpdatedBy() != null) {
            performance.setUpdatedBy(performanceDTO.getUpdatedBy());
        }

        return performanceRepository.save(performance);
    }

    @Transactional
    public void deletePerformance(Long performanceId) {
        if (!performanceRepository.existsById(performanceId)) {
            throw new RuntimeException("Performance record not found");
        }
        performanceRepository.deleteById(performanceId);
    }
}