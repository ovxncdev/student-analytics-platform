//dashboard.js API Configuration

// Check authentication
const userRole = localStorage.getItem('userRole');
const username = localStorage.getItem('username');
const token = localStorage.getItem('token');

if (!userRole || (userRole != 1 && userRole != 2)) {
    window.location.href = 'login.html';
}

// Set username in navbar
document.getElementById('teacherName').textContent = username || 'Teacher';

// Logout function
function logout() {
    localStorage.clear();
    window.location.href = 'login.html';
}

// Handle form submission
document.getElementById('predictionForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    // Get form values
    const data = {
        attendancePercentage: parseFloat(document.getElementById('attendance').value),
        quizAverage: parseFloat(document.getElementById('quiz').value),
        assignmentAverage: parseFloat(document.getElementById('assignment').value),
        midtermScore: parseFloat(document.getElementById('midterm').value),
        participationScore: parseInt(document.getElementById('participation').value),
        studyHoursPerWeek: parseFloat(document.getElementById('studyHours').value),
        previousGpa: parseFloat(document.getElementById('previousGpa').value)
    };
    
    // Show loading
    document.getElementById('loading').style.display = 'block';
    document.getElementById('results').style.display = 'none';
    document.getElementById('error').style.display = 'none';
    document.getElementById('predictBtn').disabled = true;
    
    try {
        // Call Prediction API
        const response = await fetch(`${API_BASE_URL}/api/predict/complete`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if (!response.ok) {
            throw new Error('Prediction failed');
        }
        
        const result = await response.json();
        
        // Display results
        document.getElementById('resultGrade').textContent = result.predictedGrade;
        document.getElementById('resultScore').textContent = result.predictedScore.toFixed(2);
        document.getElementById('resultRisk').textContent = result.riskPrediction;
        
        // Risk level badge
        const riskLevel = result.riskLevel;
        const badgeClass = riskLevel === 'High' ? 'badge-high' : 
                          riskLevel === 'Medium' ? 'badge-medium' : 'badge-low';
        document.getElementById('resultRiskLevel').innerHTML = 
            `<span class="badge ${badgeClass}">${riskLevel} Risk</span>`;
        
        document.getElementById('resultProb').textContent = 
            (result.passProbability * 100).toFixed(1) + '%';
        
        // Show results
        document.getElementById('results').style.display = 'block';
        
    } catch (error) {
        document.getElementById('error').textContent = 
            '❌ Error: Unable to generate prediction. Make sure the prediction service is running on port 8083.';
        document.getElementById('error').style.display = 'block';
    } finally {
        document.getElementById('loading').style.display = 'none';
        document.getElementById('predictBtn').disabled = false;
    }
});

// Load sample data for testing
function loadSampleData() {
    document.getElementById('attendance').value = 85;
    document.getElementById('quiz').value = 78;
    document.getElementById('assignment').value = 82;
    document.getElementById('midterm').value = 80;
    document.getElementById('participation').value = 7;
    document.getElementById('studyHours').value = 12;
    document.getElementById('previousGpa').value = 3.2;
}

// Load sample data on page load (for demo)
window.addEventListener('load', loadSampleData);