-- ============================================================
-- Smart Student Performance Analytics Platform
-- Schema Definition (Tables Only)
-- File: schema.sql (Place in src/main/resources/)
-- ============================================================

-- ============================================================
-- TABLE 1: users (Authentication)
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role INT NOT NULL COMMENT '1=Admin, 2=Teacher, 3=Student',
    full_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE 2: teachers (Teacher profiles)
-- ============================================================
CREATE TABLE IF NOT EXISTS teachers (
    teacher_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    employee_id VARCHAR(20) UNIQUE,
    department VARCHAR(100),
    office_location VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE 3: students (Student profiles)
-- ============================================================
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    student_number VARCHAR(20) UNIQUE NOT NULL,
    major VARCHAR(100),
    year INT COMMENT '1, 2, 3, 4',
    semester INT DEFAULT 1 COMMENT 'Current semester',
    max_courses_per_semester INT DEFAULT 3,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_student_number (student_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE 4: courses (Course information)
-- ============================================================
CREATE TABLE IF NOT EXISTS courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    teacher_id INT,
    semester VARCHAR(20) COMMENT 'e.g., Fall 2025',
    year INT,
    max_students INT DEFAULT 50,
    credits INT DEFAULT 3,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE SET NULL,
    INDEX idx_course_code (course_code),
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_semester (semester, year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE 5: enrollments (Student-Course relationships)
-- ============================================================
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    enrollment_status VARCHAR(20) DEFAULT 'active' COMMENT 'active, dropped, completed',
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completion_date TIMESTAMP NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, course_id),
    INDEX idx_student_id (student_id),
    INDEX idx_course_id (course_id),
    INDEX idx_status (enrollment_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE 6: performance (Performance metrics)
-- ============================================================
CREATE TABLE IF NOT EXISTS performance (
    performance_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    attendance_percentage DECIMAL(5,2) DEFAULT 0.00,
    quiz_average DECIMAL(5,2) DEFAULT 0.00,
    assignment_average DECIMAL(5,2) DEFAULT 0.00,
    midterm_score DECIMAL(5,2) DEFAULT 0.00,
    participation_score INT DEFAULT 0,
    study_hours_per_week INT DEFAULT 0,
    previous_gpa DECIMAL(3,2) DEFAULT 0.00,
    final_score DECIMAL(5,2) NULL,
    final_grade VARCHAR(2) NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by INT,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    FOREIGN KEY (updated_by) REFERENCES teachers(teacher_id) ON DELETE SET NULL,
    UNIQUE KEY unique_performance (student_id, course_id),
    INDEX idx_student_id (student_id),
    INDEX idx_course_id (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE 7: predictions (ML prediction results)
-- ============================================================
CREATE TABLE IF NOT EXISTS predictions (
    prediction_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    predicted_grade VARCHAR(2),
    predicted_score DECIMAL(5,2),
    risk_prediction VARCHAR(10),
    risk_level VARCHAR(10),
    confidence DECIMAL(5,4),
    pass_probability DECIMAL(5,4),
    prediction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    INDEX idx_student_id (student_id),
    INDEX idx_course_id (course_id),
    INDEX idx_risk_level (risk_level),
    INDEX idx_prediction_date (prediction_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- VIEWS
-- ============================================================

CREATE OR REPLACE VIEW view_students AS
SELECT 
    s.student_id,
    s.student_number,
    u.username,
    u.full_name,
    u.email,
    s.major,
    s.year,
    s.semester,
    u.is_active
FROM students s
JOIN users u ON s.user_id = u.user_id;

CREATE OR REPLACE VIEW view_teachers AS
SELECT 
    t.teacher_id,
    t.employee_id,
    u.username,
    u.full_name,
    u.email,
    t.department,
    t.office_location,
    u.is_active
FROM teachers t
JOIN users u ON t.user_id = u.user_id;

CREATE OR REPLACE VIEW view_enrollments AS
SELECT 
    e.enrollment_id,
    s.student_number,
    u.full_name AS student_name,
    c.course_code,
    c.course_name,
    t.full_name AS teacher_name,
    e.enrollment_status,
    e.enrollment_date
FROM enrollments e
JOIN students s ON e.student_id = s.student_id
JOIN users u ON s.user_id = u.user_id
JOIN courses c ON e.course_id = c.course_id
JOIN teachers teach ON c.teacher_id = teach.teacher_id
JOIN users t ON teach.user_id = t.user_id;

-- ============================================================
-- END OF SCHEMA
-- ============================================================