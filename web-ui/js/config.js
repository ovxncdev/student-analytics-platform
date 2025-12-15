// ============================================
// CENTRALIZED API CONFIGURATION
// ============================================
// Change USE_CLOUD to switch environments
// All JS files automatically use these settings
// ============================================

const API_CONFIG = (function() {
    // ============================================
    // DEPLOYMENT SETTINGS - CHANGE HERE ONLY
    // ============================================
    
    const CLOUD_IP = 'YOUR_SERVER_IP_HERE';  // ← Replace with server IP (e.g., '165.232.123.45')
    const USE_CLOUD = false;  // ← Set to true for cloud deployment
    
    // ============================================
    // AUTO-CONFIGURATION
    // ============================================
    
    const BASE_URL = USE_CLOUD ? `http://${CLOUD_IP}` : 'http://localhost';
    
    // Service URLs
    const SERVICES = {
        USER: `${BASE_URL}:8080`,
        COURSE: `${BASE_URL}:8081`,
        PERFORMANCE: `${BASE_URL}:8082`,
        PREDICTION: `${BASE_URL}:8083`,
        ANALYTICS: `${BASE_URL}:8084`,
        RECOMMENDATION: `${BASE_URL}:8085`
    };
    
    // Log config on load
    console.log('🔧 API Configuration:', {
        environment: USE_CLOUD ? 'CLOUD ☁️' : 'LOCAL 💻',
        baseUrl: BASE_URL,
        services: SERVICES
    });
    
    return SERVICES;
})();

// ============================================
// GLOBAL VARIABLES (Used by all JS files)
// ============================================

// Main API URLs - Used by all dashboards
const API_USER = API_CONFIG.USER;
const API_COURSE = API_CONFIG.COURSE;
const API_PERFORMANCE = API_CONFIG.PERFORMANCE;
const API_PREDICTION = API_CONFIG.PREDICTION;
const API_ANALYTICS = API_CONFIG.ANALYTICS;
const API_RECOMMENDATION = API_CONFIG.RECOMMENDATION;
const API_RECOMMENDATIONS = API_CONFIG.RECOMMENDATION; // Alias for student-portal.js

// Legacy support for old variable names
const API_BASE_URL = API_CONFIG.USER; // Used by login.js and dashboard.js