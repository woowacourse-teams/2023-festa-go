// Function to fetch data and update dataSection
function fetchDataAndUpdateDataSection() {
  fetch("/admin/api/data")
  .then(response => {
    if (!response.ok) {
      throw new Error("응답을 가져올 수 없습니다.");
    }
    return response.json();
  })
  .then(data => {
    const dataSection = document.getElementById("dataSection");

    // Clear existing data before appending new data
    dataSection.innerHTML = "";

    // 1. 축제 생성 요청 데이터 섹션
    const festivalDataDiv = createDataSection(
        "축제 목록",
        data.adminFestivalResponse
    );
    dataSection.appendChild(festivalDataDiv);

    // 2. 공연 생성 요청 데이터 섹션
    const stageDataDiv = createDataSection(
        "공연 목록",
        data.adminStageResponse
    );
    dataSection.appendChild(stageDataDiv);

    // 3. 티켓 생성 요청 데이터 섹션
    const ticketDataDiv = createDataSection(
        "티켓 목록",
        data.adminTickets
    );
    dataSection.appendChild(ticketDataDiv);
  })
  .catch(error => {
    alert(error.message);
  });
}

// Call the fetchDataAndUpdateDataSection function on page load
fetchDataAndUpdateDataSection();

function createDataSection(sectionTitle, data) {
  const dataDiv = document.createElement("div");
  dataDiv.classList.add("dataDiv"); // Add the "dataDiv" class to the data div

  const title = document.createElement("h3");
  title.textContent = sectionTitle;
  dataDiv.appendChild(title);

  const table = createTable(data);
  dataDiv.appendChild(table);

  return dataDiv;
}

function createTable(data) {
  const table = document.createElement("table");
  const tableHead = document.createElement("thead");
  const tableBody = document.createElement("tbody");

  // 테이블 헤더 생성
  const headerRow = document.createElement("tr");
  for (const key in data[0]) {
    const th = document.createElement("th");
    th.textContent = key;
    headerRow.appendChild(th);
  }
  tableHead.appendChild(headerRow);

  // 테이블 데이터 생성
  data.forEach(item => {
    const row = document.createElement("tr");
    for (const key in item) {
      const cell = document.createElement("td");

      if (typeof item[key] === "object") {
        // If the value is an object (entryTimeAmount), format it with new lines
        cell.textContent = formatEntryTimeAmount(item[key]);
        cell.style.whiteSpace = "pre-line"; // Apply white-space: pre-line; style to allow line breaks
      } else {
        // If the value is not an object, display it normally
        cell.textContent = item[key];
      }

      row.appendChild(cell);
    }
    tableBody.appendChild(row);
  });

  table.appendChild(tableHead);
  table.appendChild(tableBody);
  return table;
}

// Helper function to format entryTimeAmount object and sort by time
function formatEntryTimeAmount(entryTimeAmount) {
  // Convert the object to an array of [time, amount] pairs
  const entryTimeAmountArray = Object.entries(entryTimeAmount);

  // Sort the array based on time
  entryTimeAmountArray.sort((a, b) => a[0].localeCompare(b[0]));

  // Format the sorted array as a string with line breaks
  let formattedString = "";
  entryTimeAmountArray.forEach(([time, amount]) => {
    formattedString += `${time}: ${amount}\n`;
  });

  return formattedString;
}

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

      fetch("/admin/api/festivals", {
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
        // Fetch data again and update dataSection
        fetchDataAndUpdateDataSection();
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

      fetch("/admin/api/stages", {
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
        // Fetch data again and update dataSection
        fetchDataAndUpdateDataSection();
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

      fetch("/admin/api/tickets", {
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
        // Fetch data again and update dataSection
        fetchDataAndUpdateDataSection();
      })
      .catch(error => {
        // 오류: 알림창 표시
        alert(error.message);
      });
    });
