document.getElementById("loginForm").addEventListener("submit",
    function (event) {
      event.preventDefault();
      const formData = new FormData(event.target);
      const loginRequest = {
        username: formData.get("username"),
        password: formData.get("password"),
      };

      fetch("/admin/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(loginRequest)
      })
      .then(response => {
        if (response.ok) {
          return window.location.href = '/admin';
        } else {
          return response.json().then(data => {
            throw new Error(data.message || "해당 계정이 없거나 비밀번호가 틀립니다.");
          });
        }
      })
      .catch(error => {
        alert(error.message);
      });
    });
