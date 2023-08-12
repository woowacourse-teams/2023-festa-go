document.getElementById("signupForm").addEventListener("submit",
    function (event) {
      event.preventDefault();
      const formData = new FormData(event.target);
      const signupRequest = {
        username: formData.get("username"),
        password: formData.get("password"),
      };

      fetch("/admin/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(signupRequest)
      })
      .then(response => {
        if (response.ok) {
          event.target.reset();
          alert("계정이 생성되었습니다.");
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
