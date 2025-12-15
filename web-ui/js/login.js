// API Configuration
const API_BASE_URL = 'http://localhost:8080';

// Login form handler
document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const loginBtn = document.getElementById('loginBtn');
    const errorDiv = document.getElementById('errorMessage');
    
    // Disable button
    loginBtn.disabled = true;
    loginBtn.textContent = 'Logging in...';
    errorDiv.style.display = 'none';
    
    try {
        const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Invalid username or password');
        }
        
        const data = await response.json();
        
        // Store user info
        localStorage.setItem('token', data.token);
        localStorage.setItem('userId', data.userId);
        localStorage.setItem('username', data.username);
        localStorage.setItem('userRole', data.role);
        localStorage.setItem('fullName', data.fullName);
        
        // Redirect based on role
        // Role: 1=Admin, 2=Teacher, 3=Student
      if (data.role === 1) {
    window.location.href = 'admin-dashboard.html';
} else if (data.role === 2) {
    window.location.href = 'teacher-dashboard.html';
} else if (data.role === 3) {
    window.location.href = 'student-portal.html';
} else {
    throw new Error('Invalid user role');
}
        
    } catch (error) {
        errorDiv.textContent = '❌ ' + error.message;
        errorDiv.style.display = 'block';
        loginBtn.disabled = false;
        loginBtn.textContent = 'Login';
    }
});