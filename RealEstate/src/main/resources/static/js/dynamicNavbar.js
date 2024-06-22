document.addEventListener("DOMContentLoaded", () => {
  // Function to load the header
  function loadHeader() {
    fetch("/includes/header.html")
      .then((response) => response.text())
      .then((data) => {
        document.querySelector("header").innerHTML = data;
        applyRoleBasedVisibility();
      })
      .catch((error) => console.error("Error loading header:", error));
  }

  // Function to apply role-based visibility
  function applyRoleBasedVisibility() {
    const roles = JSON.parse(localStorage.getItem("roles")) || [];

    // Select all <li> elements that have data-roles attribute
    document.querySelectorAll("#navbar > li[data-roles]").forEach((item) => {
      const allowedRolesAttr = item.getAttribute("data-roles");

      if (allowedRolesAttr) {
        const allowedRoles = allowedRolesAttr.split(",");

        // Check if any of the roles in localStorage match the allowed roles for this item
        if (roles.some((role) => allowedRoles.includes(role.trim()))) {
          item.style.display = "block"; // Display the menu item if user has the role
        } else {
          item.style.display = "none"; // Hide the menu item if user doesn't have the role
        }
      } else {
        console.warn("data-roles attribute not found on navbar item:", item);
        // Optionally handle the case where data-roles attribute is missing
        // For example, you can decide to show or hide the item based on a default behavior
        item.style.display = "block"; // or 'none', depending on your design choice
      }
    });
  }

  // Load the header on page load
  loadHeader();
});
