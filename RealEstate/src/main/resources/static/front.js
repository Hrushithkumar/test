document.addEventListener("DOMContentLoaded", () => {
  // Fetch and display listings
  fetchListings();
  fetchHomeListings();

  // Handle contact form submission
  document
    .getElementById("contact-form")
    .addEventListener("submit", handleContactFormSubmit);

  // Handle quote form submission
  document
    .getElementById("quote-form")
    .addEventListener("submit", handleQuoteFormSubmit);

  // Handle login form submission
  document
    .getElementById("login-form")
    .addEventListener("submit", handleLoginFormSubmit);

  // Handle signup form submission
  document
    .getElementById("signup-form")
    .addEventListener("submit", handleSignupFormSubmit);

  // Handle forgot password form submission
  document
    .getElementById("forgot-password-form")
    .addEventListener("submit", handleForgotPasswordFormSubmit);

  // Add event listeners for filter dropdowns
  document
    .getElementById("project-filter")
    .addEventListener("change", filterProjects);
  document
    .getElementById("home-filter")
    .addEventListener("change", filterHomes);
});

function fetchListings() {
  // This would typically involve an API call to fetch listings
  const listings = [
    {
      id: 1,
      title: "1 BHK Apartment",
      description: "1 BHK apartments available.",
      image: "https://via.placeholder.com/300x200",
      type: "1-bhk",
    },
    {
      id: 2,
      title: "2 BHK Apartment",
      description: "2 BHK apartments available.",
      image: "https://via.placeholder.com/300x200",
      type: "2-bhk",
    },
    {
      id: 3,
      title: "3 BHK Apartment",
      description: "3 BHK apartments available.",
      image: "https://via.placeholder.com/300x200",
      type: "3-bhk",
    },
    {
      id: 4,
      title: "Commercial Project",
      description: "Office spaces, retail stores, and more.",
      image: "https://via.placeholder.com/300x200",
      type: "commercial",
    },
    {
      id: 5,
      title: "Industrial Project",
      description: "Factories, warehouses, and other industrial spaces.",
      image: "https://via.placeholder.com/300x200",
      type: "industrial",
    },
  ];

  const listingsContainer = document.getElementById("listings");
  listings.forEach((listing) => {
    const listingElement = document.createElement("div");
    listingElement.className = `project ${listing.type}`;
    listingElement.innerHTML = `
              <img src="${listing.image}" alt="${listing.title}">
              <h3>${listing.title}</h3>
              <p>${listing.description}</p>
          `;
    listingsContainer.appendChild(listingElement);
  });
}

function fetchHomeListings() {
  // This would typically involve an API call to fetch home listings
  const homes = [
    {
      id: 1,
      title: "Luxury Villa",
      description: "4 BHK, 3 Baths, 3500 sqft, pool and garden.",
      image: "https://via.placeholder.com/300x200",
      type: "luxury-villa",
    },
    {
      id: 2,
      title: "Modern Apartment",
      description: "3 BHK, 2 Baths, 1800 sqft, city view.",
      image: "https://via.placeholder.com/300x200",
      type: "modern-apartment",
    },
    {
      id: 3,
      title: "Cozy Cottage",
      description: "2 BHK, 1 Bath, 1200 sqft, countryside.",
      image: "https://via.placeholder.com/300x200",
      type: "cozy-cottage",
    },
  ];

  const homesContainer = document.getElementById("home-listings");
  homes.forEach((home) => {
    const homeElement = document.createElement("div");
    homeElement.className = `home ${home.type}`;
    homeElement.innerHTML = `
              <img src="${home.image}" alt="${home.title}">
              <h3>${home.title}</h3>
              <p>${home.description}</p>
          `;
    homesContainer.appendChild(homeElement);
  });
}

function handleContactFormSubmit(event) {
  event.preventDefault();
  const name = event.target["contact-name"].value;
  const email = event.target["contact-email"].value;
  const message = event.target["contact-message"].value;

  // Basic validation
  if (!name || !email || !message) {
    alert("Please fill out all fields.");
    return;
  }

  // Here you would typically send this data to your server
  console.log("Contact Form Submitted", { name, email, message });

  // Display success message
  alert("Thank you for contacting us! We will get back to you soon.");

  // Clear the form
  event.target.reset();
}

function handleQuoteFormSubmit(event) {
  event.preventDefault();
  const name = event.target["quote-name"].value;
  const email = event.target["quote-email"].value;
  const phone = event.target["quote-phone"].value;
  const project = event.target["quote-project"].value;
  const message = event.target["quote-message"].value;

  // Basic validation
  if (!name || !email || !phone || !project || !message) {
    alert("Please fill out all fields.");
    return;
  }

  // Here you would typically send this data to your server
  console.log("Quote Form Submitted", {
    name,
    email,
    phone,
    project,
    message,
  });

  // Simulate server response for successful form submission
  document.getElementById("quote-success").style.display = "block";
  document.getElementById("quote-error").style.display = "none";

  // Clear the form
  event.target.reset();

  // Close the modal after a short delay
  setTimeout(() => {
    var modal = bootstrap.Modal.getInstance(
      document.getElementById("quoteModal")
    );
    modal.hide();
  }, 2000);
}

async function handleLoginFormSubmit(event) {
  event.preventDefault();
  const email = event.target["login-email"].value;
  const password = event.target["login-password"].value;

  // Basic validation
  if (!email || !password) {
    alert("Please fill out all fields.");
    return;
  }

  const loginData = { email: email, password: password };

  try {
    const response = await fetch("/users/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginData),
    });

    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(errorMessage);
    }

    const data = await response.text();

    console.log("Login successful", data);
    document.getElementById("login-error").style.display = "none";
    document.getElementById("login-success").style.display = "block";

    // Store the user data in localStorage
    localStorage.setItem("user", JSON.stringify(data.user));

    // Clear the form
    event.target.reset();

    // Redirect to the services page after a short delay
    setTimeout(() => {
      window.location.href = "/services.html"; // Update this to the desired URL
    }, 2000);
  } catch (error) {
    console.error("Error:", error);
    document.getElementById("login-error").innerText = error.message;
    document.getElementById("login-error").style.display = "block";
    document.getElementById("login-success").style.display = "none";
  }
}

async function handleSignupFormSubmit(event) {
  event.preventDefault();
  const firstName = event.target["signup-firstname"].value;
  const lastName = event.target["signup-lastname"].value;
  const email = event.target["signup-email"].value;
  const confirmEmail = event.target["signup-confirm-email"].value;
  const password = event.target["signup-password"].value;
  const confirmPassword = event.target["signup-confirm-password"].value;
  const username = event.target["signup-username"].value;
  const phone = event.target["signup-phone"].value;

  // Basic validation
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

  // Here you would typically send this data to your server
  console.log("Signup Form Submitted", {
    firstName,
    lastName,
    email,
    password,
    username,
    phone,
  });

  const signupData = {
    firstName,
    lastName,
    email,
    password,
    username,
    phoneNumber: phone,
  };

  try {
    const response = await fetch("/users", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(signupData),
    });

    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(errorMessage);
    }

    const data = await response.text();

    document.getElementById("signup-success").style.display = "block";
    document.getElementById("signup-error").style.display = "none";

    // Clear the form
    event.target.reset();

    // Close the modal after a short delay
    setTimeout(() => {
      var modal = bootstrap.Modal.getInstance(
        document.getElementById("signupModal")
      );
      modal.hide();
      // Hide success message after modal is closed
      document.getElementById("signup-success").style.display = "none";
    }, 2000);
  } catch (error) {
    console.error("Error:", error);
    document.getElementById("signup-error").innerText = error.message;
    document.getElementById("signup-error").style.display = "block";
    document.getElementById("signup-success").style.display = "none";
  }
}

function handleForgotPasswordFormSubmit(event) {
  event.preventDefault();
  const email = event.target["forgot-password-email"].value;

  // Basic validation
  if (!email) {
    alert("Please fill out the email field.");
    return;
  }

  // Here you would typically send this data to your server
  console.log("Forgot Password Form Submitted", { email });

  // Simulate server response for successful password reset
  document.getElementById("forgot-password-error").style.display = "none";
  document.getElementById("forgot-password-success").style.display = "block";

  // Clear the form
  event.target.reset();

  // Close the modal after a short delay
  setTimeout(() => {
    var modal = bootstrap.Modal.getInstance(
      document.getElementById("forgotPasswordModal")
    );
    modal.hide();
  }, 2000);
}

function filterProjects() {
  const filter = document.getElementById("project-filter").value;
  const projects = document.querySelectorAll(".project");
  projects.forEach((project) => {
    if (filter === "all" || project.classList.contains(filter)) {
      project.style.display = "block";
    } else {
      project.style.display = "none";
    }
  });
}

function filterHomes() {
  const filter = document.getElementById("home-filter").value;
  const homes = document.querySelectorAll(".home");
  homes.forEach((home) => {
    if (filter === "all" || home.classList.contains(filter)) {
      home.style.display = "block";
    } else {
      home.style.display = "none";
    }
  });
}
