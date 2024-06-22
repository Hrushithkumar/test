document.addEventListener("DOMContentLoaded", () => {
  document
    .getElementById("login-form")
    .addEventListener("submit", handleLoginFormSubmit);
  document
    .getElementById("signup-form")
    .addEventListener("submit", handleSignupFormSubmit);
  document
    .getElementById("forgot-password-form")
    .addEventListener("submit", handleForgotPasswordFormSubmit);
  document
    .getElementById("contact-form")
    .addEventListener("submit", handleContactFormSubmit);

  const serviceCards = document.querySelectorAll(".service-card");
  serviceCards.forEach((card) =>
    card.addEventListener("click", handleServiceCardClick)
  );
});

function handleServiceCardClick(event) {
  const service = event.currentTarget.dataset.service;
  const modalTitle = document.getElementById("service-title");
  const modalDescription = document.getElementById("service-description");

  modalTitle.textContent = service;
  modalDescription.textContent = `Detailed information about ${service} will go here.`;

  const serviceModal = new bootstrap.Modal(
    document.getElementById("serviceModal")
  );
  serviceModal.show();
}

function handleLoginFormSubmit(event) {
  event.preventDefault();
  const email = event.target["login-email"].value;
  const password = event.target["login-password"].value;

  if (!email || !password) {
    alert("Please fill out all fields.");
    return;
  }

  fetch("/authenticate", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  })
    .then((response) => response.json())
    .then((data) => {
      const token = data.jwtToken;
      const roles = data.roles;
      if (data.error) {
        document.getElementById("login-error").innerText = data.error;
        document.getElementById("login-error").style.display = "block";
        document.getElementById("login-success").style.display = "none";
        event.target.reset();
      } else {
        document.getElementById("login-success").innerText = data.message;
        document.getElementById("login-error").style.display = "none";
        document.getElementById("login-success").style.display = "block";
        if (token) {
          localStorage.setItem("token", token); // Save token to local storage
        } else {
          console.error("Token not received from server");
        }

        if (roles) {
          localStorage.setItem("roles", JSON.stringify(roles)); // Save roles to local storage
        } else {
          console.error("Roles not received from server");
        }

        event.target.reset();
        setTimeout(() => {
          var modal = bootstrap.Modal.getInstance(
            document.getElementById("loginModal")
          );
          modal.hide();
          fetchServices();
        }, 2000);

        // Hide success and error messages after showing success
        setTimeout(() => {
          document.getElementById("login-success").style.display = "none";
          document.getElementById("login-error").style.display = "none";
        }, 2000);
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      document.getElementById("login-error").innerText = error.message;
      document.getElementById("login-error").style.display = "block";
      document.getElementById("login-success").style.display = "none";
    });

  document
    .getElementById("loginModal")
    .addEventListener("hidden.bs.modal", () => {
      document.getElementById("login-error").style.display = "none";
      document.getElementById("login-success").style.display = "none";
      document.getElementById("login-error").innerText = "";
      document.getElementById("login-success").innerText = "";
    });
}

function handleContactFormSubmit(event) {
  event.preventDefault();

  const name = event.target["contact-name"].value;
  const email = event.target["contact-email"].value;
  const message = event.target["contact-message"].value;

  if (!name || !email || !message) {
    alert("Please fill out all fields.");
    return;
  }

  fetch("/employee/contacts", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email, message }),
  })
    .then((response) => {
      if (response.ok) {
        alert("Contact form submitted successfully");
        event.target.reset();
      } else {
        alert("Failed to submit contact form");
      }
    })
    .catch((error) => console.error("Error submitting contact form:", error));
}

function fetchServices() {
  const token = localStorage.getItem("token");
  if (token) {
    window.location.href = "/main.html";
  } else {
    alert("Access Denied. Please log in.");
    window.location.href = "/";
  }
}

function handleSignupFormSubmit(event) {
  event.preventDefault();
  const firstName = event.target["signup-firstname"].value;
  const lastName = event.target["signup-lastname"].value;
  const email = event.target["signup-email"].value;
  const confirmEmail = event.target["signup-confirm-email"].value;
  const password = event.target["signup-password"].value;
  const confirmPassword = event.target["signup-confirm-password"].value;
  const username = event.target["signup-username"].value;
  const phone = event.target["signup-phone"].value;

  if (
    !firstName ||
    !lastName ||
    !email ||
    !confirmEmail ||
    !password ||
    !confirmPassword ||
    !username ||
    !phone
  ) {
    alert("Please fill out all fields.");
    return;
  }

  if (email !== confirmEmail) {
    alert("Email and Confirm Email do not match.");
    return;
  }

  if (password !== confirmPassword) {
    alert("Password and Confirm Password do not match.");
    return;
  }

  fetch("/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      firstName,
      lastName,
      email,
      password,
      username,
      phoneNumber: phone,
    }),
  })
    .then((response) => response.json())
    .then((data) => {
      const messageDiv = document.getElementById("message");
      if (data.error) {
        messageDiv.className = "message error";
        messageDiv.textContent = data.error;
      } else {
        messageDiv.className = "message success";
        messageDiv.textContent = "User created successfully ";
        event.target.reset();
        setTimeout(() => {
          var modal = bootstrap.Modal.getInstance(
            document.getElementById("signupModal")
          );
          modal.hide();
          messageDiv.style.display = "none";
        }, 2000);
      }
      messageDiv.style.display = "block";
    })
    .catch((error) => {
      const messageDiv = document.getElementById("message");
      messageDiv.className = "message error";
      messageDiv.textContent = "An unexpected error occurred: " + error.message;
      messageDiv.style.display = "block";
    });
}

function handleForgotPasswordFormSubmit(event) {
  event.preventDefault();
  const email = event.target["forgot-password-email"].value;

  if (!email) {
    alert("Please fill out the email field.");
    return;
  }

  fetch("/api/forgot-password", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email }),
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.error) {
        document.getElementById("forgot-password-error").style.display =
          "block";
        document.getElementById("forgot-password-success").style.display =
          "none";
      } else {
        document.getElementById("forgot-password-success").innerText =
          "Password reset email sent. Please check your inbox.";
        document.getElementById("forgot-password-success").style.display =
          "block";
        document.getElementById("forgot-password-error").style.display = "none";

        setTimeout(() => {
          var modal = bootstrap.Modal.getInstance(
            document.getElementById("forgotPasswordModal")
          );
          modal.hide();
        }, 2000);
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      document.getElementById("forgot-password-error").innerText =
        error.message;
      document.getElementById("forgot-password-error").style.display = "block";
      document.getElementById("forgot-password-success").style.display = "none";
    });
}
