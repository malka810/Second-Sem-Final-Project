    // register form
    $("#registerForm").submit(function (event) {
        event.preventDefault();

        var password = $("#registerPassword").val();
        var confirmPassword = $("#registerConfirmPassword").val();
        if (password !== confirmPassword) {
            alert("Passwords do not match!");
            return;
        }

        var userData = {
            fullName: $("#registerName").val(),
            email: $("#registerEmail").val(),
            password: password,
            role: $("#registerRole").val()
        };

        var formData = new FormData();
        formData.append("user", new Blob([JSON.stringify(userData)], { type: "application/json" }));
        formData.append("profileImage", $("#profileImageInput")[0].files[0]);

        $.ajax({
            type: "POST",
            url: "http://localhost:3030/api/v1/user/register",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                if (response.code === 201) { // Check if registration was successful
                    alert("Registration Successful!");

                    // Store the token if it exists
                    if (response.data && response.data.token) {
                        localStorage.setItem("authToken", response.data.token);
                    }

                    // Redirect based on role - now specifically for ADMIN
                    if (userData.role === "ADMIN") {
                        window.location.href = "../admin-dashboard.html";
                    } else if (userData.role === "INSTRUCTOR") {
                        window.location.href = "instructorDashboard.html";
                    } else {
                        window.location.href = "studentHome.html";
                    }
                } else {
                    alert("Registration failed: " + (response.message || "Unknown error"));
                }
            },
            error: function (xhr) {
                alert("Registration failed: " + (xhr.responseJSON?.message || xhr.responseText || "Unknown error"));
            }
        });
    });

    function previewImage(event) {
    const previewContainer = document.getElementById('imagePreviewContainer');
    const previewImage = document.getElementById('imagePreview');

    const file = event.target.files[0];
    if (file) {
    const reader = new FileReader();
    reader.onload = function () {
    previewImage.src = reader.result;
    previewContainer.style.display = 'block';
}
    reader.readAsDataURL(file);
}
}

// login form
    document.getElementById("loginButton").addEventListener("click", function (event) {
        event.preventDefault();

        let email = document.getElementById("loginEmail").value.trim();
        let password = document.getElementById("loginPassword").value.trim();

        if (!email || !password) {
            alert("Please enter both email and password.");
            return;
        }
        //
        // let user = {
        //     email: email,
        //     password: password
        // };

        fetch("http://localhost:3030/api/v1/auth/authenticate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        })
            .then(async response => {
                if (!response.ok) {
                    const error = await response.json();
                    throw new Error(error.message || "Login failed");
                }
                return response.json();
            })
            .then(data => {
                if (!data.data || !data.data.token || !data.data.role) {
                    throw new Error("No token or role received from server");
                }

                console.log("Login successful:", data.data);

                // Store authentication data
                localStorage.setItem("authToken", data.data.token);
                localStorage.setItem("userRole", data.data.role);
                localStorage.setItem("userEmail", data.data.email);

                // Redirect based on role
                const role = data.data.role.toUpperCase();
                switch(role) {
                    case "ADMIN":
                        window.location.href = "admin-dashboard.html";
                        break;
                    case "INSTRUCTOR":
                        window.location.href = "../Instructor/instructorDashboard.html";
                        break;
                    case "STUDENT":
                        window.location.href = "../Student/studentHome.html";
                        break;
                    default:
                        alert("Unknown user role: " + role);
                }
            })
            .catch(error => {
                console.error("Login error:", error);
                alert("Login failed: " + error.message);
            });
    });
