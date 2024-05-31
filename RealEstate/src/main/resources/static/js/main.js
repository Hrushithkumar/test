document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('register-form');
    const loginForm = document.getElementById('login-form');
    const homeDiv = document.getElementById('home');
    const logoutButton = document.getElementById('logout');

    const showHomePage = () => {
        registerForm.style.display = 'none';
        loginForm.style.display = 'none';
        homeDiv.style.display = 'block';
    };

    const showLoginPage = () => {
        registerForm.style.display = 'block';
        loginForm.style.display = 'block';
        homeDiv.style.display = 'none';
    };

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('register-username').value;
        const password = document.getElementById('register-password').value;

        try {
            const response = await fetch('http://localhost:8080/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });

            if (response.ok) {
                alert('User registered successfully');
            } else {
                alert('Error registering user');
            }
        } catch (error) {
            console.error('There was an error registering the user!', error);
        }
    });

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const email = document.getElementById('login-email').value;
        const password = document.getElementById('login-password').value;

        try {
            const response = await fetch('http://localhost:8080/authenticate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            if (response.ok) {
                const data = await response.text();
                localStorage.setItem('token', data);
                showHomePage();
            } else {
                alert('Error logging in');
            }
        } catch (error) {
            console.error('There was an error logging in!', error);
        }
    });

    logoutButton.addEventListener('click', () => {
        localStorage.removeItem('token');
        showLoginPage();
    });

    if (localStorage.getItem('token')) {
        showHomePage();
    } else {
        showLoginPage();
    }
});
