"""
Generate Synthetic Student Performance Dataset
Creates 1000 realistic student records with performance metrics
"""

import pandas as pd
import numpy as np
import random
from datetime import datetime, timedelta

# Set random seed for reproducibility
np.random.seed(42)
random.seed(42)

def generate_student_data(num_students=1000):
    """
    Generate synthetic student performance data
    
    Returns:
        DataFrame with student records
    """
    
    students = []
    
    # Sample student names
    first_names = ['John', 'Jane', 'Michael', 'Emily', 'David', 'Sarah', 'James', 'Emma', 
                   'Robert', 'Olivia', 'William', 'Ava', 'Richard', 'Isabella', 'Joseph',
                   'Sophia', 'Thomas', 'Mia', 'Charles', 'Charlotte', 'Daniel', 'Amelia',
                   'Matthew', 'Harper', 'Anthony', 'Evelyn', 'Mark', 'Abigail', 'Donald',
                   'Elizabeth', 'Steven', 'Sofia', 'Paul', 'Ella', 'Andrew', 'Madison']
    
    last_names = ['Smith', 'Johnson', 'Williams', 'Brown', 'Jones', 'Garcia', 'Miller',
                  'Davis', 'Rodriguez', 'Martinez', 'Hernandez', 'Lopez', 'Gonzalez',
                  'Wilson', 'Anderson', 'Thomas', 'Taylor', 'Moore', 'Jackson', 'Martin',
                  'Lee', 'Thompson', 'White', 'Harris', 'Clark', 'Lewis', 'Robinson']
    
    for i in range(num_students):
        student_id = f"STU{str(i+1).zfill(5)}"  # STU00001, STU00002, etc.
        
        # Generate name
        first_name = random.choice(first_names)
        last_name = random.choice(last_names)
        name = f"{first_name} {last_name}"
        email = f"{first_name.lower()}.{last_name.lower()}@student.edu"
        
        # Generate performance metrics with realistic correlations
        
        # Previous GPA influences current performance
        previous_gpa = np.random.normal(2.8, 0.6)
        previous_gpa = np.clip(previous_gpa, 0.0, 4.0)
        
        # Study hours correlate with GPA
        study_hours = np.random.normal(10 + (previous_gpa - 2.0) * 2, 4)
        study_hours = np.clip(study_hours, 0, 20)
        
        # Attendance influenced by previous GPA and study habits
        attendance_base = 70 + (previous_gpa / 4.0) * 20
        attendance = np.random.normal(attendance_base, 10)
        attendance = np.clip(attendance, 0, 100)
        
        # Quiz average - influenced by attendance and study hours
        quiz_base = 50 + (attendance / 100 * 30) + (study_hours / 20 * 20)
        quiz_average = np.random.normal(quiz_base, 12)
        quiz_average = np.clip(quiz_average, 0, 100)
        
        # Assignment average - similar to quiz but slightly higher
        assignment_base = quiz_base + 5
        assignment_average = np.random.normal(assignment_base, 10)
        assignment_average = np.clip(assignment_average, 0, 100)
        
        # Midterm score - weighted combination
        midterm_base = (quiz_average * 0.4 + assignment_average * 0.4 + attendance / 100 * 20)
        midterm_score = np.random.normal(midterm_base, 13)
        midterm_score = np.clip(midterm_score, 0, 100)
        
        # Participation - correlated with attendance
        participation_base = 5 + (attendance / 100 * 5)
        participation = int(np.clip(np.random.normal(participation_base, 2), 1, 10))
        
        # Calculate final score (weighted average)
        final_score = (
            attendance * 0.15 +
            quiz_average * 0.25 +
            assignment_average * 0.25 +
            midterm_score * 0.25 +
            participation * 10 * 0.10
        )
        
        # Add some randomness
        final_score += np.random.normal(0, 3)
        final_score = np.clip(final_score, 0, 100)
        
        # Determine letter grade
        if final_score >= 90:
            grade = 'A'
        elif final_score >= 80:
            grade = 'B'
        elif final_score >= 70:
            grade = 'C'
        elif final_score >= 60:
            grade = 'D'
        else:
            grade = 'F'
        
        # Pass/Fail
        pass_fail = 'Pass' if final_score >= 70 else 'Fail'
        
        # Round values
        attendance = round(attendance, 2)
        quiz_average = round(quiz_average, 2)
        assignment_average = round(assignment_average, 2)
        midterm_score = round(midterm_score, 2)
        study_hours = round(study_hours, 1)
        previous_gpa = round(previous_gpa, 2)
        final_score = round(final_score, 2)
        
        student = {
            'student_id': student_id,
            'name': name,
            'email': email,
            'attendance_percentage': attendance,
            'quiz_average': quiz_average,
            'assignment_average': assignment_average,
            'midterm_score': midterm_score,
            'participation_score': participation,
            'study_hours_per_week': study_hours,
            'previous_gpa': previous_gpa,
            'final_score': final_score,
            'final_grade': grade,
            'pass_fail': pass_fail
        }
        
        students.append(student)
    
    df = pd.DataFrame(students)
    return df

def generate_dataset_statistics(df):
    """Print dataset statistics"""
    print("\n" + "="*60)
    print("DATASET STATISTICS")
    print("="*60)
    
    print(f"\nTotal Students: {len(df)}")
    
    print("\n--- Grade Distribution ---")
    grade_counts = df['final_grade'].value_counts().sort_index()
    for grade, count in grade_counts.items():
        percentage = (count / len(df)) * 100
        print(f"{grade}: {count:4d} students ({percentage:5.2f}%)")
    
    print("\n--- Pass/Fail Distribution ---")
    pass_counts = df['pass_fail'].value_counts()
    for status, count in pass_counts.items():
        percentage = (count / len(df)) * 100
        print(f"{status}: {count:4d} students ({percentage:5.2f}%)")
    
    print("\n--- Performance Metrics (Mean ± Std) ---")
    metrics = ['attendance_percentage', 'quiz_average', 'assignment_average', 
               'midterm_score', 'participation_score', 'study_hours_per_week', 
               'previous_gpa', 'final_score']
    
    for metric in metrics:
        mean = df[metric].mean()
        std = df[metric].std()
        min_val = df[metric].min()
        max_val = df[metric].max()
        print(f"{metric:25s}: {mean:6.2f} ± {std:5.2f} (min: {min_val:6.2f}, max: {max_val:6.2f})")
    
    print("\n--- Correlation Analysis ---")
    corr_with_final = df[metrics].corr()['final_score'].sort_values(ascending=False)
    print("\nCorrelation with Final Score:")
    for metric, corr in corr_with_final.items():
        if metric != 'final_score':
            print(f"  {metric:25s}: {corr:6.3f}")
    
    print("\n" + "="*60)

def main():
    print("Generating Student Performance Dataset...")
    print("="*60)
    
    # Generate dataset
    num_students = 1000
    df = generate_student_data(num_students)
    
    # Save to CSV
    output_file = 'student_data.csv'
    df.to_csv(output_file, index=False)
    print(f"\n✅ Dataset saved to: {output_file}")
    
    # Print statistics
    generate_dataset_statistics(df)
    
    # Show sample records
    print("\n--- Sample Records (First 5) ---")
    pd.set_option('display.max_columns', None)
    pd.set_option('display.width', None)
    print(df.head())
    
    print("\n✅ Data generation complete!")
    print(f"   - {num_students} student records created")
    print(f"   - Saved to: {output_file}")
    print(f"   - Ready for model training!")

if __name__ == "__main__":
    main()