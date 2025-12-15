//teacher-dashboard API Configuration

// Check authentication
const userRole = localStorage.getItem('userRole');
const username = localStorage.getItem('username');
const userId = localStorage.getItem('userId');
const token = localStorage.getItem('token');

if (!userRole || userRole != 2) {
    window.location.href = 'login.html';
}

// Set username in navbar
document.getElementById('teacherName').textContent = username || 'Teacher';

// Logout function
function logout() {
    localStorage.clear();
    window.location.href = 'login.html';
}

// Global variables
let currentCourseId = null;
let currentStudentId = null;

// Load teacher's courses on page load
window.addEventListener('load', async function() {
    await loadMyCourses();
});

// Get teacher ID from username
function getTeacherId(username) {
    const mapping = {
        'prof.smith': 1,
        'prof.johnson': 2,
        'prof.williams': 3
    };
    return mapping[username] || 1;
}

// Load teacher's courses
async function loadMyCourses() {
    try {
        const teacherId = getTeacherId(username);
        const response = await fetch(`${API_COURSE}/api/courses/teacher/${teacherId}`);
        
        if (!response.ok) throw new Error('Failed to load courses');
        
        const courses = await response.json();
        
        const tableBody = document.getElementById('coursesTable');
        
        if (courses.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 20px;">You are not teaching any courses</td></tr>';
            return;
        }
        
        tableBody.innerHTML = courses.map(course => {
            return `
                <tr>
                    <td><strong>${course.courseCode}</strong></td>
                    <td>${course.courseName}</td>
                    <td>${course.semester} ${course.year}</td>
                    <td id="enrolled_${course.courseId}">Loading...</td>
                    <td>
                        <button onclick="viewStudents(${course.courseId}, '${course.courseName}')" 
                                style="padding: 5px 15px; background: #667eea; color: white; border: none; border-radius: 4px; cursor: pointer;">
                            View Students
                        </button>
                    </td>
                </tr>
            `;
        }).join('');
        
        // Load enrollment count for each course
        for (const course of courses) {
            loadEnrollmentCount(course.courseId);
        }
        
    } catch (error) {
        console.error('Error loading courses:', error);
        document.getElementById('coursesTable').innerHTML = 
            '<tr><td colspan="5" style="text-align: center; padding: 20px; color: #e74c3c;">Error loading courses.</td></tr>';
    }
}

// Load enrollment count
async function loadEnrollmentCount(courseId) {
    try {
        const response = await fetch(`${API_COURSE}/api/enrollments/course/${courseId}`);
        if (response.ok) {
            const enrollments = await response.json();
            const active = enrollments.filter(e => e.enrollmentStatus === 'active').length;
            document.getElementById(`enrolled_${courseId}`).textContent = active;
        }
    } catch (error) {
        document.getElementById(`enrolled_${courseId}`).textContent = '-';
    }
}

// View students in course
async function viewStudents(courseId, courseName) {
    currentCourseId = courseId;
    document.getElementById('selectedCourseName').textContent = courseName;
    document.getElementById('studentsCard').style.display = 'block';
    document.getElementById('gradeCard').style.display = 'none';
    if (document.getElementById('predictionCard')) {
        document.getElementById('predictionCard').style.display = 'none';
    }
    
    try {
        const enrollResponse = await fetch(`${API_COURSE}/api/enrollments/course/${courseId}`);
        if (!enrollResponse.ok) throw new Error('Failed to load students');
        
        const enrollments = await enrollResponse.json();
        const activeEnrollments = enrollments.filter(e => e.enrollmentStatus === 'active');
        
        const tableBody = document.getElementById('studentsTable');
        
        if (activeEnrollments.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="4" style="text-align: center; padding: 20px;">No students enrolled</td></tr>';
            return;
        }
        
        // Load student names and performance data
        const studentsHTML = await Promise.all(activeEnrollments.map(async (enrollment) => {
            const studentId = enrollment.studentId;
            
            // Get student name (simplified - in production use API)
            const studentName = `Student ${studentId}`;
            
            // Get current grade if exists
            let currentGrade = '-';
            try {
                const perfResponse = await fetch(`${API_PERFORMANCE}/api/performance/student/${studentId}`);
                if (perfResponse.ok) {
                    const performances = await perfResponse.json();
                    const perf = performances.find(p => p.courseId === courseId);
                    if (perf && perf.midtermScore) {
                        currentGrade = perf.midtermScore;
                    }
                }
            } catch (e) {}
            
            return `
                <tr>
                    <td>${studentId}</td>
                    <td>${studentName}</td>
                    <td>${currentGrade}</td>
                    <td>
                        <button onclick="inputGrade(${studentId}, '${studentName}')" 
                                style="padding: 5px 15px; background: #2ecc71; color: white; border: none; border-radius: 4px; cursor: pointer; margin-right: 5px;">
                            Input Grade
                        </button>
                        <button onclick="predictStudent(${studentId}, '${studentName}')" 
                                style="padding: 5px 15px; background: #667eea; color: white; border: none; border-radius: 4px; cursor: pointer;">
                            Generate Prediction
                        </button>
                    </td>
                </tr>
            `;
        }));
        
        tableBody.innerHTML = studentsHTML.join('');
        
    } catch (error) {
        console.error('Error loading students:', error);
        document.getElementById('studentsTable').innerHTML = 
            '<tr><td colspan="4" style="text-align: center; padding: 20px; color: #e74c3c;">Error loading students.</td></tr>';
    }
}

// Show grade input form
function inputGrade(studentId, studentName) {
    currentStudentId = studentId;
    document.getElementById('selectedStudentName').textContent = studentName;
    document.getElementById('gradeCard').style.display = 'block';
    if (document.getElementById('predictionCard')) {
        document.getElementById('predictionCard').style.display = 'none';
    }
    
    // Load existing grade if available
    loadExistingGrade(studentId, currentCourseId);
}

// Load existing grade
async function loadExistingGrade(studentId, courseId) {
    try {
        const response = await fetch(`${API_PERFORMANCE}/api/performance/student/${studentId}`);
        if (response.ok) {
            const performances = await response.json();
            const perf = performances.find(p => p.courseId === courseId);
            
            if (perf) {
                document.getElementById('attendance').value = perf.attendancePercentage || '';
                document.getElementById('quizAvg').value = perf.quizAverage || '';
                document.getElementById('assignmentAvg').value = perf.assignmentAverage || '';
                document.getElementById('midterm').value = perf.midtermScore || '';
                document.getElementById('participation').value = perf.participationScore || '';
                document.getElementById('studyHours').value = perf.studyHoursPerWeek || '';
                document.getElementById('previousGpa').value = perf.previousGpa || '';
            }
        }
    } catch (error) {
        console.error('Error loading existing grade:', error);
    }
}

// Hide grade form
function hideGradeForm() {
    document.getElementById('gradeCard').style.display = 'none';
    document.getElementById('gradeForm').reset();
}

// Submit grade
async function submitGrade(event) {
    event.preventDefault();
    
    const performanceData = {
        studentId: currentStudentId,
        courseId: currentCourseId,
        attendancePercentage: parseFloat(document.getElementById('attendance').value),
        quizAverage: parseFloat(document.getElementById('quizAvg').value),
        assignmentAverage: parseFloat(document.getElementById('assignmentAvg').value),
        midtermScore: parseFloat(document.getElementById('midterm').value),
        participationScore: parseInt(document.getElementById('participation').value),
        studyHoursPerWeek: parseFloat(document.getElementById('studyHours').value),
        previousGpa: parseFloat(document.getElementById('previousGpa').value),
        updatedBy: getTeacherId(username)
    };
    
    try {
        // Check if performance exists
        const checkResponse = await fetch(`${API_PERFORMANCE}/api/performance/student/${currentStudentId}`);
        let existingPerf = null;
        let performanceId = null;
        
        if (checkResponse.ok) {
            const performances = await checkResponse.json();
            existingPerf = performances.find(p => p.courseId === currentCourseId);
            if (existingPerf) {
                performanceId = existingPerf.performanceId;
            }
        }
        
        let response;
        if (performanceId) {
            // Update existing
            response = await fetch(`${API_PERFORMANCE}/api/performance/${performanceId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(performanceData)
            });
        } else {
            // Create new
            response = await fetch(`${API_PERFORMANCE}/api/performance`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(performanceData)
            });
        }
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to save grade');
        }
        
        alert('✅ Grade saved successfully!');
        
        // Also create prediction record
        try {
            await createPrediction(currentStudentId, currentCourseId, performanceData);
        } catch (e) {
            console.error('Failed to create prediction:', e);
        }
        
        hideGradeForm();
        await viewStudents(currentCourseId, document.getElementById('selectedCourseName').textContent);
        
    } catch (error) {
        alert('❌ Error: ' + error.message);
    }
}

// Generate prediction for student
async function predictStudent(studentId, studentName) {
    document.getElementById('predictedStudentName').textContent = studentName;
    document.getElementById('predictionCard').style.display = 'block';
    document.getElementById('gradeCard').style.display = 'none';
    document.getElementById('recommendationsBox').innerHTML = '<p style="color: #999;">Loading...</p>';
    
    try {
        // Get student's performance data
        const perfResponse = await fetch(`${API_PERFORMANCE}/api/performance/student/${studentId}`);
        if (!perfResponse.ok) throw new Error('No performance data found');
        
        const performances = await perfResponse.json();
        const perf = performances.find(p => p.courseId === currentCourseId);
        
        if (!perf) {
            throw new Error('No performance data for this course. Please input grades first.');
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
        await loadRecommendations(studentId);
        
    } catch (error) {
        alert('❌ Error: ' + error.message);
        document.getElementById('predictionCard').style.display = 'none';
    }
}

// Load recommendations
async function loadRecommendations(studentId) {
    try {
        console.log(`Fetching recommendations for student ${studentId}, course ${currentCourseId}`);
        
        const response = await fetch(`${API_RECOMMENDATION}/api/recommendations/student/${studentId}/course/${currentCourseId}`);
        
        console.log('Response status:', response.status);
        
        if (!response.ok) {
            // If API fails, generate recommendations from performance data
            console.log('API failed, generating recommendations from performance data');
            await generateRecommendationsFromPerformance(studentId);
            return;
        }
        
        const data = await response.json();
        console.log('Recommendations data:', data);
        
        displayRecommendations(data);
        
    } catch (error) {
        console.error('Full error:', error);
        // Try to generate from performance data
        await generateRecommendationsFromPerformance(studentId);
    }
}

// Display recommendations HTML
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
        <h4 style="margin-top: 15px; margin-bottom: 10px; color: #667eea;">📋 Action Items:</h4>
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

// Generate recommendations from performance data (fallback)
async function generateRecommendationsFromPerformance(studentId) {
    try {
        const perfResponse = await fetch(`${API_PERFORMANCE}/api/performance/student/${studentId}`);
        if (!perfResponse.ok) throw new Error('No performance data');
        
        const performances = await perfResponse.json();
        const perf = performances.find(p => p.courseId === currentCourseId);
        
        if (!perf) throw new Error('No performance data for this course');
        
        // Generate recommendations based on performance
        const recommendations = [];
        let riskLevel = 'Low';
        let priority = 'LOW';
        
        const attendance = parseFloat(perf.attendancePercentage);
        const quizAvg = parseFloat(perf.quizAverage);
        const assignmentAvg = parseFloat(perf.assignmentAverage);
        const midterm = parseFloat(perf.midtermScore);
        const participation = parseInt(perf.participationScore);
        const studyHours = parseFloat(perf.studyHoursPerWeek);
        
        // Determine risk level
        const avgScore = (quizAvg + assignmentAvg + midterm) / 3;
        if (avgScore < 60 || attendance < 70) {
            riskLevel = 'High';
            priority = 'URGENT';
        } else if (avgScore < 75 || attendance < 80) {
            riskLevel = 'Medium';
            priority = 'MODERATE';
        }
        
        // Generate specific recommendations
        if (attendance < 80) {
            recommendations.push(`Improve attendance - current: ${attendance.toFixed(1)}%`);
        }
        if (quizAvg < 70) {
            recommendations.push(`Focus on quiz preparation - current average: ${quizAvg.toFixed(1)}`);
        }
        if (assignmentAvg < 70) {
            recommendations.push(`Complete assignments on time - current average: ${assignmentAvg.toFixed(1)}`);
        }
        if (midterm < 70) {
            recommendations.push(`Seek additional tutoring - midterm score: ${midterm.toFixed(1)}`);
        }
        if (participation < 5) {
            recommendations.push(`Increase class participation - current score: ${participation}/10`);
        }
        if (studyHours < 10) {
            recommendations.push(`Increase study time - currently studying ${studyHours} hours/week`);
        }
        
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
        
        const data = {
            riskLevel: riskLevel,
            priority: priority,
            recommendations: recommendations
        };
        
        displayRecommendations(data);
        
    } catch (error) {
        console.error('Failed to generate recommendations:', error);
        document.getElementById('recommendationsBox').innerHTML = 
            `<p style="color: #e74c3c;">Unable to generate recommendations. Please ensure performance data is complete.</p>`;
    }
}

// Close prediction card
function closePrediction() {
    document.getElementById('predictionCard').style.display = 'none';
}

// Create prediction record for recommendations
async function createPrediction(studentId, courseId, performanceData) {
    try {
        const predData = {
            attendancePercentage: performanceData.attendancePercentage,
            quizAverage: performanceData.quizAverage,
            assignmentAverage: performanceData.assignmentAverage,
            midtermScore: performanceData.midtermScore,
            participationScore: performanceData.participationScore,
            studyHoursPerWeek: performanceData.studyHoursPerWeek,
            previousGpa: performanceData.previousGpa
        };
        
        const predResponse = await fetch(`${API_PREDICTION}/api/predict/complete`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(predData)
        });
        
        if (predResponse.ok) {
            const prediction = await predResponse.json();
            console.log('Prediction created:', prediction);
        }
    } catch (error) {
        console.error('Error creating prediction:', error);
    }
}