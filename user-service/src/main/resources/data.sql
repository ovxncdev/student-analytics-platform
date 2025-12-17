-- Insert users
INSERT IGNORE INTO users (user_id, username, password_hash, email, role, full_name) VALUES 
(1, 'admin', 'admin123', 'admin@university.edu', 1, 'System Administrator'),
(2, 'prof.smith', 'teacher123', 'jsmith@university.edu', 2, 'Dr. John Smith'),
(3, 'prof.johnson', 'teacher123', 'ejohnson@university.edu', 2, 'Dr. Emily Johnson'),
(4, 'prof.williams', 'teacher123', 'mwilliams@university.edu', 2, 'Prof. Michael Williams'),
(5, 'john.doe', 'student123', 'jdoe@student.edu', 3, 'John Doe'),
(6, 'jane.smith', 'student123', 'jsmith@student.edu', 3, 'Jane Smith'),
(7, 'bob.wilson', 'student123', 'bwilson@student.edu', 3, 'Bob Wilson'),
(8, 'alice.brown', 'student123', 'abrown@student.edu', 3, 'Alice Brown'),
(9, 'charlie.davis', 'student123', 'cdavis@student.edu', 3, 'Charlie Davis');

-- Insert teachers
INSERT IGNORE INTO teachers (teacher_id, user_id, employee_id, department, office_location, phone) VALUES 
(1, 2, 'T001', 'Computer Science', 'Building A, Room 301', '555-0101'),
(2, 3, 'T002', 'Computer Science', 'Building A, Room 302', '555-0102'),
(3, 4, 'T003', 'Mathematics', 'Building B, Room 201', '555-0103');

-- Insert students
INSERT IGNORE INTO students (student_id, user_id, student_number, major, year, semester) VALUES 
(1, 5, 'S2021001', 'Computer Science', 2, 3),
(2, 6, 'S2021002', 'Computer Science', 2, 3),
(3, 7, 'S2021003', 'Software Engineering', 3, 5),
(4, 8, 'S2021004', 'Computer Science', 1, 1),
(5, 9, 'S2021005', 'Data Science', 2, 3);

-- Insert courses
INSERT IGNORE INTO courses (course_id, course_code, course_name, teacher_id, semester, year, credits) VALUES 
(1, 'CS101', 'Introduction to Programming', 1, 'Fall', 2025, 3),
(2, 'CS201', 'Data Structures', 1, 'Fall', 2025, 3),
(3, 'CS301', 'Database Systems', 2, 'Fall', 2025, 3),
(4, 'MATH201', 'Calculus II', 3, 'Fall', 2025, 4),
(5, 'CS401', 'Machine Learning', 2, 'Fall', 2025, 3);

-- Insert enrollments
INSERT IGNORE INTO enrollments (enrollment_id, student_id, course_id, enrollment_status) VALUES 
(1, 1, 1, 'active'),
(2, 1, 3, 'active'),
(3, 1, 4, 'active'),
(4, 2, 1, 'active'),
(5, 2, 2, 'active'),
(6, 2, 3, 'active'),
(7, 3, 2, 'active'),
(8, 3, 3, 'active'),
(9, 3, 5, 'active'),
(10, 4, 1, 'active'),
(11, 4, 4, 'active'),
(12, 5, 1, 'active'),
(13, 5, 2, 'active'),
(14, 5, 5, 'active');

-- Insert performance
INSERT IGNORE INTO performance (performance_id, student_id, course_id, attendance_percentage, quiz_average, assignment_average, midterm_score, participation_score, study_hours_per_week, previous_gpa, updated_by) VALUES 
(1, 1, 1, 92.0, 85.5, 88.0, 87.0, 8, 12, 3.5, 1),
(2, 1, 3, 88.0, 82.0, 85.5, 84.0, 7, 10, 3.5, 2),
(3, 1, 4, 95.0, 90.0, 92.0, 89.0, 9, 15, 3.5, 3),
(4, 2, 1, 98.0, 95.0, 96.0, 94.0, 10, 18, 3.9, 1),
(5, 2, 2, 96.0, 93.0, 94.5, 92.0, 9, 16, 3.9, 1),
(6, 2, 3, 94.0, 91.0, 93.0, 90.0, 9, 14, 3.9, 2),
(7, 3, 2, 75.0, 70.0, 72.5, 68.0, 5, 8, 2.8, 1),
(8, 3, 3, 78.0, 73.0, 75.0, 71.0, 6, 9, 2.8, 2),
(9, 3, 5, 80.0, 75.0, 77.5, 73.0, 6, 10, 2.8, 2);

-- Insert predictions
INSERT IGNORE INTO predictions (prediction_id, student_id, course_id, predicted_grade, predicted_score, risk_prediction, risk_level, confidence, pass_probability) VALUES 
(1, 1, 1, 'B', 87.2, 'Pass', 'Low', 0.9234, 0.9234),
(2, 1, 3, 'B', 84.5, 'Pass', 'Low', 0.8876, 0.8876),
(3, 1, 4, 'A', 90.8, 'Pass', 'Low', 0.9567, 0.9567),
(4, 2, 1, 'A', 95.1, 'Pass', 'Low', 0.9876, 0.9876),
(5, 2, 2, 'A', 93.7, 'Pass', 'Low', 0.9745, 0.9745),
(6, 2, 3, 'A', 91.9, 'Pass', 'Low', 0.9654, 0.9654),
(7, 3, 2, 'C', 71.2, 'Pass', 'Medium', 0.7123, 0.7123),
(8, 3, 3, 'C', 73.8, 'Pass', 'Medium', 0.7456, 0.7456),
(9, 3, 5, 'C', 75.4, 'Pass', 'Medium', 0.7678, 0.7678);