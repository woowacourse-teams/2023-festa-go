// 축제 생성 버튼 클릭 시 요청 보내기
document.getElementById("createFestivalForm").addEventListener("submit",
    function (event) {
      event.preventDefault();
      const formData = new FormData(event.target);
      const festivalData = {
        name: formData.get("name"),
        startDate: formData.get("startDate"),
        endDate: formData.get("endDate"),
        thumbnail: formData.get("thumbnail")
      };

      fetch("/admin/festivals", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(festivalData)
      })
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          return response.json().then(data => {
            throw new Error(data.message || "축제 생성에 실패하였습니다.");
          });
        }
      })
      .then(data => {
        // 성공: 알림창 표시하고 폼 필드 초기화
        alert("축제가 성공적으로 생성되었습니다!");
        document.getElementById("createFestivalForm").reset();
      })
      .catch(error => {
        // 오류: 알림창 표시
        alert(error.message);
      });
    });

// 공연 생성 버튼 클릭 시 요청 보내기
document.getElementById("createPerformanceForm").addEventListener("submit",
    function (event) {
      event.preventDefault();
      const formData = new FormData(event.target);
      const performanceData = {
        startTime: formData.get("startTime"),
        lineUp: formData.get("lineUp"),
        ticketOpenTime: formData.get("ticketOpenTime"),
        festivalId: formData.get("festivalId")
      };

      fetch("/admin/stages", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(performanceData)
      })
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          return response.json().then(data => {
            throw new Error(data.message || "공연 생성에 실패하였습니다.");
          });
        }
      })
      .then(data => {
        // 성공: 알림창 표시하고 폼 필드 초기화
        alert("공연이 성공적으로 생성되었습니다!");
        document.getElementById("createPerformanceForm").reset();
      })
      .catch(error => {
        // 오류: 알림창 표시
        alert(error.message);
      });
    });

// 티켓 생성 버튼 클릭 시 요청 보내기
document.getElementById("createTicketForm").addEventListener("submit",
    function (event) {
      event.preventDefault();
      const formData = new FormData(event.target);
      const ticketData = {
        stageId: formData.get("stageId"),
        ticketType: formData.get("ticketType"),
        amount: formData.get("amount"),
        entryTime: formData.get("entryTime")
      };

      fetch("/admin/tickets", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(ticketData)
      })
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          return response.json().then(data => {
            throw new Error(data.message || "티켓 생성에 실패하였습니다.");
          });
        }
      })
      .then(data => {
        // 성공: 알림창 표시하고 폼 필드 초기화
        alert("티켓이 성공적으로 생성되었습니다!");
        document.getElementById("createTicketForm").reset();
      })
      .catch(error => {
        // 오류: 알림창 표시
        alert(error.message);
      });
    });
