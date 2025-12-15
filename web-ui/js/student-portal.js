// API Configuration
const API_PERFORMANCE = 'http://localhost:8082';
const API_ANALYTICS = 'http://localhost:8084';
const API_RECOMMENDATIONS = 'http://localhost:8085';
const API_COURSE = 'http://localhost:8081';
const API_PREDICTION = 'http://localhost:8083';

// Check authentication
const userRole = localStorage.getItem('userRole');
const username = localStorage.getItem('username');
const userId = localStorage.getItem('userId');
const token = localStorage.getItem('token');

if (!userRole || userRole != 3) {
    window.location.href = 'login.html';
}

// Set username in navbar
document.getElementById('studentName').textContent = username || 'Student';

// Logout function
function logout() {
    localStorage.clear();
    window.location.href = 'login.html';
}

// Global variable
let myStudentId = null;

// Load student data on page load
window.addEventListener('load', async function() {
    myStudentId = getStudentId(username);
    await loadStudentStats();
    await loadAvailableCourses();
    await loadMyEnrollments();
});

// Helper function to map username to student_id
function getStudentId(username) {
    const mapping = {
        'john.doe': 1,
        'jane.smith': 2,
        'bob.wilson': 3,
        'alice.brown': 4,
        'charlie.davis': 5
    };
    return mapping[username] || 1;
}

// Load student statistics
async function loadStudentStats() {
    try {
        // Get performance data
        const perfResponse = await fetch(`${API_PERFORMANCE}/api/performance/student/${myStudentId}`);
        
        if (perfResponse.ok) {
            const performances = await perfResponse.json();
            
            if (performances.length > 0) {
                // Calculate averages
                const avgGPA = performances.reduce((sum, p) => sum + parseFloat(p.previousGpa || 0), 0) / performances.length;
                const avgAttendance = performances.reduce((sum, p) => sum + parseFloat(p.attendancePercentage || 0), 0) / performances.length;
                const avgScore = performances.reduce((sum, p) => sum + parseFloat(p.midtermScore || 0), 0) / performances.length;
                
                document.getElementById('gpaValue').textContent = avgGPA.toFixed(2);
                document.getElementById('attendanceValue').textContent = avgAttendance.toFixed(0) + '%';
                document.getElementById('scoreValue').textContent = avgScore.toFixed(1);
            }
        }
        
        // Get enrollment count
        const enrollResponse = await fetch(`${API_COURSE}/api/enrollments/student/${myStudentId}/active`);
        if (enrollResponse.ok) {
            const enrollments = await enrollResponse.json();
            document.getElementById('enrolledValue').textContent = enrollments.length;
        }
        
    } catch (error) {
        console.error('Error loading student stats:', error);
    }
}

// Load available courses
async function loadAvailableCourses() {
    try {
        // Get all courses
        const coursesResponse = await fetch(`${API_COURSE}/api/courses`);
        if (!coursesResponse.ok) throw new Error('Failed to load courses');
        
        const allCourses = await coursesResponse.json();
        
        // Get my enrollments
        const enrollResponse = await fetch(`${API_COURSE}/api/enrollments/student/${myStudentId}`);
        let myEnrollments = [];
        if (enrollResponse.ok) {
            myEnrollments = await enrollResponse.json();
        }
        
        // Filter out courses I'm already enrolled in
        const enrolledCourseIds = myEnrollments.map(e => e.courseId);
        const availableCourses = allCourses.filter(c => !enrolledCourseIds.includes(c.courseId) && c.isActive);
        
        const tableBody = document.getElementById('availableCoursesTable');
        
        if (availableCourses.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 20px;">No available courses to enroll</td></tr>';
            return;
        }
        
        // Check if student can enroll (max 3 courses)
        const activeEnrollments = myEnrollments.filter(e => e.enrollmentStatus === 'active');
        const canEnroll = activeEnrollments.length < 3;
        
        tableBody.innerHTML = availableCourses.map(course => {
            const enrollBtn = canEnroll 
                ? `<button onclick="enrollCourse(${course.courseId})" style="padding: 5px 15px; background: #2ecc71; color: white; border: none; border-radius: 4px; cursor: pointer;">Enroll</button>`
                : `<span style="color: #999;">Max courses reached</span>`;
            
            return `
                <tr>
                    <td><strong>${course.courseCode}</strong></td>
                    <td>${course.courseName}</td>
                    <td>Teacher ${course.teacherId}</td>
                    <td>${course.credits}</td>
                    <td>${course.semester} ${course.year}</td>
                    <td>${enrollBtn}</td>
                </tr>
            `;
        }).join('');
        
    } catch (error) {
        console.error('Error loading available courses:', error);
        document.getElementById('availableCoursesTable').innerHTML = 
            '<tr><td colspan="6" style="text-align: center; padding: 20px; color: #e74c3c;">Error loading courses.</td></tr>';
    }
}

// Load my enrollments
async function loadMyEnrollments() {
    try {
        const enrollResponse = await fetch(`${API_COURSE}/api/enrollments/student/${myStudentId}`);
        if (!enrollResponse.ok) throw new Error('Failed to load enrollments');
        
        const enrollments = await enrollResponse.json();
        const activeEnrollments = enrollments.filter(e => e.enrollmentStatus === 'active');
        
        const tableBody = document.getElementById('enrolledCoursesTable');
        
        if (activeEnrollments.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 20px;">You are not enrolled in any courses</td></tr>';
            return;
        }
        
        // Load course details and performance for each enrollment
        const enrollmentsHTML = await Promise.all(activeEnrollments.map(async (enrollment) => {
            // Get course details
            const courseResponse = await fetch(`${API_COURSE}/api/courses/${enrollment.courseId}`);
            const course = courseResponse.ok ? await courseResponse.json() : null;
            
            // Get performance data
            let currentGrade = '-';
            let attendance = '-';
            let hasPerformance = false;
            
            try {
                const perfResponse = await fetch(`${API_PERFORMANCE}/api/performance/student/${myStudentId}`);
                if (perfResponse.ok) {
                    const performances = await perfResponse.json();
                    const perf = performances.find(p => p.courseId === enrollment.courseId);
                    if (perf) {
                        currentGrade = perf.midtermScore ? perf.midtermScore.toFixed(1) : '-';
                        attendance = perf.attendancePercentage ? perf.attendancePercentage.toFixed(0) + '%' : '-';
                        hasPerformance = true;
                    }
                }
            } catch (e) {}
            
            const statusBadge = '<span style="background: #d4edda; color: #27ae60; padding: 4px 12px; border-radius: 12px; font-size: 12px;">Active</span>';
            
            const viewBtn = hasPerformance
                ? `<button onclick="viewPrediction(${enrollment.courseId}, '${course ? course.courseName : 'Course'}')" style="padding: 5px 15px; background: #667eea; color: white; border: none; border-radius: 4px; cursor: pointer;">View Prediction</button>`
                : '<span style="color: #999;">No grades yet</span>';
            
            return `
                <tr>
                    <td><strong>${course ? course.courseCode : '-'}</strong></td>
                    <td>${course ? course.courseName : '-'}</td>
                    <td>${currentGrade}</td>
                    <td>${attendance}</td>
                    <td>${statusBadge}</td>
                    <td>${viewBtn}</td>
                </tr>
            `;
        }));
        
        tableBody.innerHTML = enrollmentsHTML.join('');
        
    } catch (error) {
        console.error('Error loading enrollments:', error);
        document.getElementById('enrolledCoursesTable').innerHTML = 
            '<tr><td colspan="6" style="text-align: center; padding: 20px; color: #e74c3c;">Error loading your courses.</td></tr>';
    }
}

// Enroll in course
async function enrollCourse(courseId) {
    try {
        // Check current enrollments
        const checkResponse = await fetch(`${API_COURSE}/api/enrollments/student/${myStudentId}/active`);
        if (checkResponse.ok) {
            const activeEnrollments = await checkResponse.json();
            if (activeEnrollments.length >= 3) {
                alert('❌ You cannot enroll in more than 3 courses per semester');
                return;
            }
        }
        
        const enrollmentData = {
            studentId: myStudentId,
            courseId: courseId,
            enrollmentStatus: 'active'
        };
        
        const response = await fetch(`${API_COURSE}/api/enrollments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(enrollmentData)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to enroll');
        }
        
        alert('✅ Successfully enrolled in course!');
        await loadStudentStats();
        await loadAvailableCourses();
        await loadMyEnrollments();
        
    } catch (error) {
        alert('❌ Error: ' + error.message);
    }
}

// View prediction for a course
async function viewPrediction(courseId, courseName) {
    document.getElementById('selectedCourseName').textContent = courseName;
    document.getElementById('predictionCard').style.display = 'block';
    document.getElementById('recommendationsBox').innerHTML = '<p style="color: #999;">Loading...</p>';
    
    try {
        // Get performance data
        const perfResponse = await fetch(`${API_PERFORMANCE}/api/performance/student/${myStudentId}`);
        if (!perfResponse.ok) throw new Error('No performance data found');
        
        const performances = await perfResponse.json();
        const perf = performances.find(p => p.courseId === courseId);
        
        if (!perf) {
            throw new Error('No performance data for this course');
        }
        
        // Call prediction API
        const predData = {
            attendancePercentage: parseFloat(perf.attendancePercentage),
            quizAverage: parseFloat(perf.quizAverage),
            assignmentAverage: parseFloat(perf.assignmentAverage),
            midtermScore: parseFloat(perf.midtermScore),
            participationScore: parseInt(perf.participationScore),
            studyHoursPerWeek: parseFloat(perf.studyHoursPerWeek),
            previousGpa: parseFloat(perf.previousGpa)
        };
        
        const predResponse = await fetch(`${API_PREDICTION}/api/predict/complete`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(predData)
        });
        
        if (!predResponse.ok) throw new Error('Prediction failed');
        
        const prediction = await predResponse.json();
        
        // Display prediction results
        document.getElementById('predGrade').textContent = prediction.predictedGrade;
        document.getElementById('predScore').textContent = prediction.predictedScore.toFixed(2);
        document.getElementById('predRisk').textContent = prediction.riskPrediction;
        document.getElementById('predProb').textContent = (prediction.passProbability * 100).toFixed(1) + '%';
        
        // Update risk color
        const riskLevel = prediction.riskLevel;
        const riskColor = riskLevel === 'High' ? '#e74c3c' : riskLevel === 'Medium' ? '#f39c12' : '#27ae60';
        document.getElementById('predRisk').style.color = riskColor;
        
        // Load recommendations
        await loadRecommendations(courseId, perf);
        
    } catch (error) {
        alert('❌ Error: ' + error.message);
        document.getElementById('predictionCard').style.display = 'none';
    }
}

// Load recommendations
async function loadRecommendations(courseId, perf) {
    try {
        const response = await fetch(`${API_RECOMMENDATIONS}/api/recommendations/student/${myStudentId}/course/${courseId}`);
        
        if (!response.ok) {
            // Generate from performance data
            generateRecommendationsFromPerformance(perf);
            return;
        }
        
        const data = await response.json();
        displayRecommendations(data);
        
    } catch (error) {
        generateRecommendationsFromPerformance(perf);
    }
}

// Display recommendations
function displayRecommendations(data) {
    const priority = data.priority;
    const priorityColor = priority === 'URGENT' ? '#e74c3c' : priority === 'MODERATE' ? '#f39c12' : '#27ae60';
    const priorityBg = priority === 'URGENT' ? '#ffe6e6' : priority === 'MODERATE' ? '#fff3cd' : '#d4edda';
    
    let html = `
        <div style="margin-bottom: 15px;">
            <span style="background: ${priorityBg}; color: ${priorityColor}; padding: 5px 15px; border-radius: 15px; font-weight: 600;">
                ${priority} Priority
            </span>
            <span style="margin-left: 15px; color: #666;">Risk Level: <strong>${data.riskLevel}</strong></span>
        </div>
        <h4 style="margin-top: 15px; margin-bottom: 10px; color: #667eea;">📋 What You Should Do:</h4>
        <ul style="list-style: none; padding: 0;">
    `;
    
    data.recommendations.forEach(rec => {
        const isUrgent = rec.includes('URGENT') || priority === 'URGENT';
        const itemStyle = isUrgent ? 'background: #ffe6e6; border-left: 3px solid #e74c3c;' : 'background: white; border-left: 3px solid #667eea;';
        html += `<li style="${itemStyle} padding: 12px; margin-bottom: 8px; border-radius: 4px;">${rec}</li>`;
    });
    
    html += '</ul>';
    
    document.getElementById('recommendationsBox').innerHTML = html;
}

// Generate recommendations from performance
function generateRecommendationsFromPerformance(perf) {
    const recommendations = [];
    let riskLevel = 'Low';
    let priority = 'LOW';
    
    const attendance = parseFloat(perf.attendancePercentage);
    const quizAvg = parseFloat(perf.quizAverage);
    const assignmentAvg = parseFloat(perf.assignmentAverage);
    const midterm = parseFloat(perf.midtermScore);
    const participation = parseInt(perf.participationScore);
    const studyHours = parseFloat(perf.studyHoursPerWeek);
    
    const avgScore = (quizAvg + assignmentAvg + midterm) / 3;
    if (avgScore < 60 || attendance < 70) {
        riskLevel = 'High';
        priority = 'URGENT';
    } else if (avgScore < 75 || attendance < 80) {
        riskLevel = 'Medium';
        priority = 'MODERATE';
    }
    
    if (attendance < 80) recommendations.push(`Improve attendance - current: ${attendance.toFixed(1)}%`);
    if (quizAvg < 70) recommendations.push(`Focus on quiz preparation - current average: ${quizAvg.toFixed(1)}`);
    if (assignmentAvg < 70) recommendations.push(`Complete assignments on time - current average: ${assignmentAvg.toFixed(1)}`);
    if (midterm < 70) recommendations.push(`Seek additional tutoring - midterm score: ${midterm.toFixed(1)}`);
    if (participation < 5) recommendations.push(`Increase class participation - current score: ${participation}/10`);
    if (studyHours < 10) recommendations.push(`Increase study time - currently studying ${studyHours} hours/week`);
    
    if (riskLevel === 'High') {
        recommendations.push('URGENT: Schedule meeting with academic advisor');
        recommendations.push('Consider enrolling in tutoring program');
    } else if (riskLevel === 'Medium') {
        recommendations.push('Meet with instructor during office hours');
        recommendations.push('Form or join a study group');
    } else {
        recommendations.push('Maintain current study habits');
        recommendations.push('Continue attending classes regularly');
    }
    
    displayRecommendations({ riskLevel, priority, recommendations });
}

// Close prediction card
function closePrediction() {
    document.getElementById('predictionCard').style.display = 'none';
}