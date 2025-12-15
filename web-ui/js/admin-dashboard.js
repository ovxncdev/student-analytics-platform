// API Configuration
const API_COURSE = 'http://localhost:8081';
const API_USER = 'http://localhost:8080';
const API_ANALYTICS = 'http://localhost:8084';

// Check authentication
const userRole = localStorage.getItem('userRole');
const username = localStorage.getItem('username');
const token = localStorage.getItem('token');

if (!userRole || userRole != 1) {
    window.location.href = 'login.html';
}

// Set username in navbar
document.getElementById('adminName').textContent = username || 'Admin';

// Logout function
function logout() {
    localStorage.clear();
    window.location.href = 'login.html';
}

// Load data on page load
window.addEventListener('load', async function() {
    await loadAnalytics();
    await loadUsers();
    await loadCourses();
});
// Load system analytics
async function loadAnalytics() {
    try {
        const response = await fetch(`${API_ANALYTICS}/api/analytics/overview`);
        
        if (!response.ok) throw new Error('Failed to load analytics');
        
        const data = await response.json();
        
        // Update stat cards
        document.getElementById('totalStudents').textContent = data.totalStudents || 0;
        document.getElementById('totalCourses').textContent = data.totalCourses || 0;
        document.getElementById('totalEnrollments').textContent = data.totalEnrollments || 0;
        
        // Count teachers (need to get from users API)
        const usersResponse = await fetch(`${API_USER}/api/users`, {
    headers: {
        'Authorization': `Bearer ${token}`
    }
});
        if (usersResponse.ok) {
            const users = await usersResponse.json();
            const teachers = users.filter(u => u.role === 2);
            document.getElementById('totalTeachers').textContent = teachers.length;
        }
        
        // Display risk distribution
        if (data.riskDistribution) {
            const riskHTML = `
                <div style="display: flex; justify-content: space-around; align-items: center;">
                    <div>
                        <h3 style="color: #27ae60; font-size: 36px;">${data.riskDistribution.Low || 0}</h3>
                        <p>Low Risk</p>
                    </div>
                    <div>
                        <h3 style="color: #f39c12; font-size: 36px;">${data.riskDistribution.Medium || 0}</h3>
                        <p>Medium Risk</p>
                    </div>
                    <div>
                        <h3 style="color: #e74c3c; font-size: 36px;">${data.riskDistribution.High || 0}</h3>
                        <p>High Risk</p>
                    </div>
                </div>
            `;
            document.getElementById('riskInfo').innerHTML = riskHTML;
        }
        
    } catch (error) {
        console.error('Error loading analytics:', error);
        document.getElementById('riskInfo').innerHTML = '<p style="color: #e74c3c;">Error loading analytics</p>';
    }
}

// Load all users
// Load all users
async function loadUsers() {
    try {
        const response = await fetch(`${API_USER}/api/users`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (!response.ok) throw new Error('Failed to load users');
        
        const users = await response.json();
        
        const tableBody = document.getElementById('usersTable');
        
        if (users.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="7" style="text-align: center; padding: 20px;">No users found</td></tr>';
            return;
        }
        
        tableBody.innerHTML = users.map(user => {
            const roleName = user.role === 1 ? 'Admin' : user.role === 2 ? 'Teacher' : 'Student';
            const roleColor = user.role === 1 ? '#667eea' : user.role === 2 ? '#2ecc71' : '#f39c12';
            const statusBadge = user.isActive 
                ? '<span style="background: #d4edda; color: #27ae60; padding: 4px 12px; border-radius: 12px; font-size: 12px;">Active</span>'
                : '<span style="background: #f8d7da; color: #e74c3c; padding: 4px 12px; border-radius: 12px; font-size: 12px;">Inactive</span>';
            
            return `
                <tr>
                    <td>${user.userId}</td>
                    <td><strong>${user.username}</strong></td>
                    <td>${user.fullName}</td>
                    <td>${user.email}</td>
                    <td><span style="color: ${roleColor}; font-weight: 600;">${roleName}</span></td>
                    <td>${statusBadge}</td>
                    <td>
                        <button onclick="deleteUser(${user.userId})" style="padding: 5px 10px; font-size: 12px; background: #e74c3c; color: white; border: none; border-radius: 4px; cursor: pointer;">Delete</button>
                    </td>
                </tr>
            `;
        }).join('');
        
    } catch (error) {
        console.error('Error loading users:', error);
        document.getElementById('usersTable').innerHTML = 
            '<tr><td colspan="7" style="text-align: center; padding: 20px; color: #e74c3c;">Error loading users. Make sure you are logged in as admin.</td></tr>';
    }
}

// Show/hide add user form
function showAddUserForm() {
    document.getElementById('addUserForm').style.display = 'block';
}

function hideAddUserForm() {
    document.getElementById('addUserForm').style.display = 'none';
    document.getElementById('newUserForm').reset();
}

// Create new user
async function createUser(event) {
    event.preventDefault();
    
    const userData = {
        username: document.getElementById('newUsername').value,
        password: document.getElementById('newPassword').value,
        fullName: document.getElementById('newFullName').value,
        email: document.getElementById('newEmail').value,
        role: parseInt(document.getElementById('newRole').value)
    };
    
    try {
        const response = await fetch(`${API_USER}/api/users`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(userData)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to create user');
        }
        
        alert('✅ User created successfully!');
        hideAddUserForm();
        await loadUsers();
        
    } catch (error) {
        alert('❌ Error: ' + error.message);
    }
}

// Delete user
async function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_USER}/api/users/${userId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (!response.ok) {
            throw new Error('Failed to delete user');
        }
        
        alert('✅ User deleted successfully!');
        await loadUsers();
        
    } catch (error) {
        alert('❌ Error: ' + error.message);
    }
}
// Load all courses
async function loadCourses() {
    try {
        const response = await fetch(`${API_COURSE}/api/courses`);
        
        if (!response.ok) throw new Error('Failed to load courses');
        
        const courses = await response.json();
        
        const tableBody = document.getElementById('coursesTable');
        
        if (courses.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="7" style="text-align: center; padding: 20px;">No courses found</td></tr>';
            return;
        }
        
        tableBody.innerHTML = courses.map(course => {
            return `
                <tr>
                    <td>${course.courseId}</td>
                    <td><strong>${course.courseCode}</strong></td>
                    <td>${course.courseName}</td>
                    <td>${course.teacherId}</td>
                    <td>${course.semester} ${course.year}</td>
                    <td>${course.credits}</td>
                    <td>
                        <button onclick="deleteCourse(${course.courseId})" style="padding: 5px 10px; font-size: 12px; background: #e74c3c; color: white; border: none; border-radius: 4px; cursor: pointer;">Delete</button>
                    </td>
                </tr>
            `;
        }).join('');
        
    } catch (error) {
        console.error('Error loading courses:', error);
        document.getElementById('coursesTable').innerHTML = 
            '<tr><td colspan="7" style="text-align: center; padding: 20px; color: #e74c3c;">Error loading courses.</td></tr>';
    }
}

// Show/hide add course form
function showAddCourseForm() {
    document.getElementById('addCourseForm').style.display = 'block';
}

function hideAddCourseForm() {
    document.getElementById('addCourseForm').style.display = 'none';
    document.getElementById('newCourseForm').reset();
}

// Create new course
async function createCourse(event) {
    event.preventDefault();
    
    const courseData = {
        courseCode: document.getElementById('newCourseCode').value,
        courseName: document.getElementById('newCourseName').value,
        teacherId: parseInt(document.getElementById('newTeacherId').value),
        semester: document.getElementById('newSemester').value,
        year: parseInt(document.getElementById('newYear').value),
        credits: parseInt(document.getElementById('newCredits').value)
    };
    
    try {
        const response = await fetch(`${API_COURSE}/api/courses`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(courseData)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to create course');
        }
        
        alert('✅ Course created successfully!');
        hideAddCourseForm();
        await loadCourses();
        
    } catch (error) {
        alert('❌ Error: ' + error.message);
    }
}

// Delete course
async function deleteCourse(courseId) {
    if (!confirm('Are you sure you want to delete this course?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_COURSE}/api/courses/${courseId}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            throw new Error('Failed to delete course');
        }
        
        alert('✅ Course deleted successfully!');
        await loadCourses();
        
    } catch (error) {
        alert('❌ Error: ' + error.message);
    }
}