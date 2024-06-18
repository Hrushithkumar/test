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
        window.location.href = '/';
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
        window.location.href = '/';
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

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("roles")
    window.location.href = "/";
}

window.onload = function() {
    const jwt = localStorage.getItem("token");
    const roles = localStorage.getItem("roles");
    // console.log("JWT token is " + jwt);
    // console.log("Role is " + roles);
    //
    // const navigationLinksContainer = document.getElementById("navigation-links");
    //
    // // Function to create a list item with a link
    // function createLinkListItem(link, text) {
    //     const li = document.createElement("li");
    //     const a = document.createElement("a");
    //     a.href = link;
    //     a.textContent = text;
    //     li.appendChild(a);
    //     return li;
    // }
    //
    // // Add links based on the user's role
    // if (roles.includes("ROLE_ADMIN")) {
    //     // Add admin-specific links
    //     navigationLinksContainer.appendChild(createLinkListItem("/usermanagement.html", "User Management"));
    //     navigationLinksContainer.appendChild(createLinkListItem("/employeemanagement.html", "Employee Management"));
    // } else if (roles.includes("ROLE_EMPLOYEE")) {
    //     // Add employee-specific links
    //     navigationLinksContainer.appendChild(createLinkListItem("/QuotePage.html", "Quotes"));
    //     navigationLinksContainer.appendChild(createLinkListItem("/contactuspage.html", "Contacts"));
    //     navigationLinksContainer.appendChild(createLinkListItem("/ListServiceRequests.html", "Service Requests"));
    // } else {
    //     // Add default links for regular users
    //     navigationLinksContainer.appendChild(createLinkListItem("/services.html", "Home"));
    //     navigationLinksContainer.appendChild(createLinkListItem("/uploadproject.html", "Upload Project"));
    //     navigationLinksContainer.appendChild(createLinkListItem("/servicespage.html", "Service Request"));
    //     navigationLinksContainer.appendChild(createLinkListItem("/uploadhomeforsales.html", "Upload Home for Sale"));
    //     navigationLinksContainer.appendChild(createLinkListItem("/services.html#contact-form", "Contact Us"));
    //     navigationLinksContainer.appendChild(createLinkListItem("/services.html#quote-form", "Get Quote"));
    // }
}