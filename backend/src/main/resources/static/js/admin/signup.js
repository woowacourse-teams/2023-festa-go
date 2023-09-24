document.getElementById("signupForm").addEventListener("submit",
    function (event) {
      event.preventDefault();
      const formData = new FormData(event.target);
      const username = formData.get("username");
      const password = formData.get("password");
      const confirmPassword = formData.get("confirmPassword");
      if (password !== confirmPassword) {
        alert("비밀번호와 확인 비밀번호가 맞지 않습니다!")
        return;
      }

      const signupRequest = {
        username: username,
        password: password,
      };

      fetch("/admin/api/signup", {
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
