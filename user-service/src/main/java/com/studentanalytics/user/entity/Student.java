package com.studentanalytics.user.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "student_number", unique = true, nullable = false, length = 20)
    private String studentNumber;

    @Column(name = "major", length = 100)
    private String major;

    @Column(name = "year")
    private Integer year;

    @Column(name = "semester")
    private Integer semester = 1;

    @Column(name = "max_courses_per_semester")
    private Integer maxCoursesPerSemester = 3;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // Constructors
    public Student() {
    }

    public Student(Long userId, String studentNumber, String major, Integer year, Integer semester) {
        this.userId = userId;
        this.studentNumber = studentNumber;
        this.major = major;
        this.year = year;
        this.semester = semester;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Integer getMaxCoursesPerSemester() {
        return maxCoursesPerSemester;
    }

    public void setMaxCoursesPerSemester(Integer maxCoursesPerSemester) {
        this.maxCoursesPerSemester = maxCoursesPerSemester;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", userId=" + userId +
                ", studentNumber='" + studentNumber + '\'' +
                ", major='" + major + '\'' +
                ", year=" + year +
                ", semester=" + semester +
                '}';
    }
}