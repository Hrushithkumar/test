function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    } catch (e) {
        return null;
    }
}

function removeExpiredToken() {
    const token = localStorage.getItem('token');
    if (!token) {
        return;
    }

    const decodedToken = parseJwt(token);
    if (!decodedToken || !decodedToken.exp) {
        localStorage.removeItem('token');
        return;
    }

    const currentTime = Math.floor(Date.now() / 1000);
    if (decodedToken.exp < currentTime) {
        localStorage.removeItem('token');
        alert("Session expired. Please login again.");
        window.location.href = '/login';
    }
}

function setupAutoTokenRemoval() {
    removeExpiredToken(); // Initial check on page load
    setInterval(removeExpiredToken, 60000); // Check every 1 minute
}

document.addEventListener('DOMContentLoaded', setupAutoTokenRemoval);

function fetchWithAuth(url, options = {}) {
    removeExpiredToken();

    const token = localStorage.getItem('token');
    if (!token) {
        alert("Session expired. Please login again.");
        window.location.href = '/login';
        return Promise.reject(new Error("No token found"));
    }

    options.headers = {
        ...options.headers,
        'Authorization': `Bearer ${token}`
    };

    return fetch(url, options)
        .then(response => {
            if (response.status === 401) {
                alert("Session expired. Please login again.");
                localStorage.removeItem('token');
                window.location.href = '/';
                return Promise.reject(new Error("Token expired"));
            }
            return response;
        })
        .catch(error => {
            console.error('Error:', error);
            throw error;
        });
}