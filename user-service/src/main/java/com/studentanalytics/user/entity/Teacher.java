package com.studentanalytics.user.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "employee_id", unique = true, length = 20)
    private String employeeId;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "office_location", length = 100)
    private String officeLocation;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // Constructors
    public Teacher() {
    }

    public Teacher(Long userId, String employeeId, String department, String officeLocation, String phone) {
        this.userId = userId;
        this.employeeId = employeeId;
        this.department = department;
        this.officeLocation = officeLocation;
        this.phone = phone;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    // Getters and Setters
    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId=" + teacherId +
                ", userId=" + userId +
                ", employeeId='" + employeeId + '\'' +
                ", department='" + department + '\'' +
                ", officeLocation='" + officeLocation + '\'' +
                '}';
    }
}