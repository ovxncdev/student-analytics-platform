---
title:  'CP630 project proposal<br>Smart Student Performance Analytics Platform'
author: Adeniyi Ridwan Adetunji
date: 2025-11-18
---

## Introduction

Every institutions face important challenges in identifying struggling students before it's too late. grading systems provide feedback only after assessments are completed, leaving small time for intervention. Research shows that immature identification of at risk students can improve pass rates by 15-20% through timely intervention. 

The Smart Student Performance Analytics Platform is a data oriented enterprise computing application that leverages machine learning to predict student performance, identify at risk students, and provide personalized recommendations. By analyzing binary performance indicators including attendance, quiz scores, assignment grades, participation , and study habits , the platform generates true predictions and actionable insights for educators. 


This project will be built with the knowledge gained from the enterprise computing course, The goal is to create a comprehensive enterprise computing solution that addresses a real world educational problem while demonstrating mastery of data processing , enterprise Java technologies, and deployment practices .

Target Users:
- Primary: Teachers and academic advisors for early intervention
- Secondary: Students seeking performance insights and recommendations
- Tertiary: Department heads for analytics and decision-making


## Problem solving and algorithms

1. Application data

Data Representation and Dataset Design . 
The dataset contains student performance records with the next structure:. 

Input Features (8 attributes):. 
- student_id: Unique identifier (Integer). 
- attendance_percentage: Class attendance (0-100%). 
- quiz_average: Average quiz score (0-100 ). 
- assignment_average: Average assignment score (0-100 ). 
- midterm_score: Midterm exam score (0-100 ). 
- participation_score: Class participation (1-10 scale). 
- study_hours_per_week: Self reported study time (0-20 hours). 
- previous_gpa: Prior semester GPA (0.0-4 .0). 

Target Variables:. 
- final_grade: Letter grade (A, B , C , D, F) - Classification target. 
- final_score: Numeric score (0-100 ) - Regression target . 
- pass_fail: Binary outcome (Pass/Fail) - Risk assessment target . 

Dataset Characteristics:. 
- Total instances: 1000+ student records . 
- Class distribution: Approximately balanced (Pass: ~75%, Fail: ~25%). 
- Feature types: Continuous (scores , percentages) and unconditional (grade). 
- Format: CSV for naked data , JSON for polished data and models

2. Models to represent information, knowledge and patterns. 
Model 1: Grade Classification Model . 
- Algorithm: logistical Regression / J48 Decision Tree . 
- Purpose: Predict letter grade (A/B/C/D/F). 
- Expected Accuracy: >85%. 
Model 2: Score Prediction Model . 
- Algorithm: Linear Regression . 
- Purpose: Predict denotive last score (0-100 ). 
- Expected MAE: <5 points . 
Model 3: Risk Assessment Model. 
- Algorithm: Binary logistical Regression . 
- Purpose: Identify at risk students (Pass/Fail). 
- Expected Accuracy: >90%. 
Model 4: Student Clustering Model (Bonus). 
- Algorithm: K-Means Clustering. 
- Purpose: Group students by performance patterns . 
- Clusters: 3 groups (High/Medium/Low performers). 
Model Storage: All models serialized in JSON format for easygoing loading in Java services.

3. Algorithms to solve the problems and compute the models.
Step 1: Data Loading and Exploration . 
-> Load student_data .csv . 
-> Descriptive statistics and visualization . 
Step 2: Data Preprocessing . 
-> Normalize/standardize features . 
-> Encode unconditional variables. 
-> Split dataset (80% train , 20% test). 
Step 3: Model Training. 
-> Train Grade Classifier (Logistic Regression). 
-> Train Score Predictor (Linear Regression). 
-> Train Risk Assessor (Binary Classification). 
-> Train Student Clusterer (K-Means). 
Step 4: Model Evaluation. 
-> Calculate accuracy, precision , recall , F1-score . 
-> Cross validation (10 fold). 
-> Select best hyperparameters. 
Step 5: Model Serialization. 
-> Export models to JSON format. 
-> Save in models/ directory
Tools: Python 3.9+ with scikit-learn,pandas,numpy, WEKA(optional) for validation

## Proposed System Design

1. System design of your solutions as an enterprise computing application.

The platform follows a microservices architecture with three Spring Boot services deployed as Docker containers, providing REST APIs for prediction , analytics, and recommendations.

2. System architecture (better use a diagram to illustrate the components and their relations. 

Service Components:. 
Prediction Service (Spring Boot). 
- Load ML models from JSON files . 
- REST API endpoints for grade/score/risk prediction. 
- Batch prediction capability. 

Analytics Service (Spring Boot). 
- Calculate class statistics . 
- Track student progress over time . 
- Generate performance reports . 

Recommendation Service (Spring Boot). 
- Generate personalized study recommendations . 
- Suggest resources based on faint areas . 
- Rule based testimonial engine . 

Frontend (Web Application). 
- Teacher Dashboard: student list , at risk alerts , class statistics, prediction form. 
- Student Portal: view predictions, recommendations, progress tracking. 
- Technologies: HTML5 , CSS3 (Bootstrap 5), JavaScript, Chart.js . 

Database:. 
- MySQL for production (student records, predictions). 
- H2 for development/testing . 
- Redis for caching (optional)

3. Platform and tools to be used in the project. 
Backend Technologies:. 
- Java 11+. 
- Spring Boot 2.7+. 
- Spring Framework (Web , Data JPA , REST). 
- Maven 3.8+ for build management. 
- MySQL 8.0 / H2 Database . 

Machine Learning:. 
- Python 3.9+ (scikit-learn , pandas , numpy). 
- Weka API (optional for validation). 
- Google Colab for experimentation . 

Frontend:. 
- HTML5, CSS3, JavaScript. 
- Bootstrap 5 for susceptible design . 
- Chart.js for data visualization . 

Deployment:. 
- Docker 24 .0+ for containerization. 
- Docker Compose for orchestration. 
- Embedded Tomcat (Spring Boot). 

Development Tools:. 
- Eclipse / VS Code. 
- Git + GitHub for version control. 
- Boomrang/Postman for API testing

## Project plan and schedule

List of tasks/milestones/check points of your project with time schedule. For team project, it needs to provide the roles and tasks of each member.   


| Task ID | Description   |  Due date | Lead   |  
| :----:  | :------------ | :-----:   | :------: |  
|  1      | Project research & team up | Day 7 of week 9 | all members | 
|  2      | Project proposal | Day 6 of week 10 | all members |
|  3      | Problem solving, dataset  | Day 6 of week 11 | Adeniyi Ridwan  |
|  4      | Algorithms and model R7D  | Day 6 of week 13 | Adeniyi Ridwan  |
|  5      | Component development     | Day 6 of week 14 | Adeniyi Ridwan   |
|  6      | Component development     | Day 6 of week 14 | Adeniyi Ridwan   | 
 

## References

1. Romero , C., & Ventura, S . (2020). "Educational Data Mining and Learning Analytics: An Updated Survey ." Wiley Interdisciplinary Reviews: Data Mining and Knowledge Discovery, 10(3). 2. Kotsiantis , S. B . (2012). "Use of Machine Learning Techniques for Educational Proposes ." Artificial Intelligence Review, 37(4), 331-344 . 
3. Gray , G ., McGuinness , C., & Owende , P. (2014). "An Application of Classification Models to Predict Learner Progression ." IEEE International Advance Computing Conference . 
4. UCI Machine Learning Repository - Student Performance Dataset: https://archive .ics.uci.edu/ml/datasets/student+performance. 
5. Spring Boot Documentation: https://spring.io/projects/spring-boot. 
6. Docker Documentation: https://docs.docker.com/. Scikit learn Documentation: https://scikit-learn.org/. 
7. Weka 3: Machine Learning Software in Java: https://www.cs.waikato.ac.nz/ml/weka/
8. CP630OC Labs and Assignments


## Appendices

1. Check the project [readme.txt](readme.txt) for detailed requirements and evaluation. 
You need to describe and claim new features in your project, so it is good (not required) to mention some new features in the proposal. For example, 

- You can introduce more security features such as password encryption and HTTPS for web component access. 
- You can try domain deployment WildFly for load balancing. We only did standalone deployment.
- You can try to deploy on Spring boot based microservice component on Docker or K8S.
- You can use Big Data tool for model computing (will be covered in weeks 10-12). 

        
2. You may use html elements and css for better presentation of your proposal. For example, this html document is generated by using [proposal_template.md](proposal_template.md) and  [proposal.css](proposal.css) and the following pandoc command   

~~~
pandoc  -s  -i proposal_template.md -o proposal_template.html --css=proposal.css
~~~

When can submit your proposal in html with css and image files or the PDF file of your proposal.  
