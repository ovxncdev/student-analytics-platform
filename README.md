\# Student Analytics Platform



ML-Powered Student Performance Prediction System



\## 🎓 Course Information

\- \*\*Course:\*\* CP630 - Enterprise Computing

\- \*\*Student ID:\*\* 245852450

\- \*\*Institution:\*\* Wilfrid Laurier University

\- \*\*Semester:\*\* Fall 2025



\## 🏗️ Architecture

Microservices-based system with 6 services:

1\. User Service (8080) - Authentication \& user management

2\. Course Service (8081) - Course \& enrollment management

3\. Performance Service (8082) - Grade tracking

4\. Prediction Service (8083) - ML predictions (scikit-learn)

5\. Analytics Service (8084) - Statistics \& analytics

6\. Recommendation Service (8085) - Personalized recommendations



\## 🛠️ Tech Stack

\- \*\*Backend:\*\* Java 8, Spring Boot 2.7.18

\- \*\*Database:\*\* MySQL 8.0

\- \*\*ML Models:\*\* Python 3, scikit-learn

\- \*\*Frontend:\*\* HTML5, CSS3, JavaScript (Vanilla)

\- \*\*Deployment:\*\* Docker, Docker Compose



\## 🚀 Quick Start



\### Prerequisites

\- Docker \& Docker Compose

\- 4GB RAM minimum

\- Ports 8080-8085 available



\### Run with Docker

```bash

docker-compose up -d

```



\### Access

\- Web UI: Open `web-ui/login.html` in browser

\- Admin: `admin` / `admin123`

\- Teacher: `prof.smith` / `teacher123`

\- Student: `john.doe` / `student123`



\## 📊 Features

\- ✅ Role-based dashboards (Admin, Teacher, Student)

\- ✅ ML-powered grade predictions

\- ✅ Risk assessment \& early warnings

\- ✅ Personalized study recommendations

\- ✅ Course enrollment management (max 3 per semester)

\- ✅ Real-time analytics \& statisticss



\## 📂 Project Structure

```

project/

├── user-service/          # Port 8080

├── course-service/        # Port 8081

├── performance-service/   # Port 8082

├── prediction-service/    # Port 8083

├── analytics-service/     # Port 8084

├── recommendation-service/# Port 8085

├── web-ui/               # Frontend

├── docker-compose.yml    # Docker orchestration

└── DOCKER\_DEPLOYMENT.md  # Deployment guide

```



\## 🗄️ Database Schema

7 tables: users, teachers, students, courses, enrollments, performance, predictions



\## 🤖 ML Models

\- Grade Classifier (Logistic Regression)

\- Score Predictor (Linear Regression)

\- Risk Assessor (Logistic Regression)



\## 📝 Documentation

\- \[Docker Deployment Guide](DOCKER\_DEPLOYMENT.md)

\- API Documentation: See individual service README files



\## 🎯 Project Score

Target: 250/250 points



\## 📧 Contact

Student ID: 245852450

