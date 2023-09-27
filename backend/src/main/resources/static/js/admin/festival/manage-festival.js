import {validateFestival} from "./common-festival.js"

function fetchFestivals() {
  const festivalGrid = document.getElementById("festivalGrid");

  fetch("/festivals").then(res => {
    if (!res.ok) {
      throw new Error("서버에 연결할 수 없습니다.")
    }
    return res.json();
  }).then(data => {
    const festivals = data.festivals;
    for (const festival of festivals) {
      const row = document.createElement("div");
      row.classList.add("row", "align-items-center", "gx-0", "py-1",
          "border-top");

      const idColumn = document.createElement("div");
      idColumn.classList.add("col-1");
      idColumn.textContent = festival.id;

      const schoolIdColumn = document.createElement("div");
      schoolIdColumn.classList.add("col-1");
      schoolIdColumn.textContent = festival.schoolId;

      const nameColumn = document.createElement("div");
      nameColumn.classList.add("col-2");
      nameColumn.textContent = festival.name;

      const thumbnailColumn = document.createElement("div");
      thumbnailColumn.classList.add("col-2");
      thumbnailColumn.textContent = festival.thumbnail;

      const startDateColumn = document.createElement("div");
      startDateColumn.classList.add("col-2");
      startDateColumn.textContent = festival.startDate;

      const endDateColumn = document.createElement("div");
      endDateColumn.classList.add("col-2");
      endDateColumn.textContent = festival.endDate;

      const buttonColumn = document.createElement("div");
      buttonColumn.classList.add("col-2")
      const button = document.createElement("a");
      button.classList.add("btn", "btn-primary");
      button.setAttribute("href", `festivals/${festival.id}`);
      button.textContent = "편집";
      buttonColumn.append(button);

      row.append(idColumn, schoolIdColumn, nameColumn, thumbnailColumn,
          startDateColumn, endDateColumn, buttonColumn);
      festivalGrid.append(row);
    }
  })
}

fetchFestivals();

function fetchSchools() {
  const schoolSelect = document.getElementById("schoolSelect");

  fetch("/schools").then(res => {
    if (!res.ok) {
      throw new Error("서버에 연결할 수 없습니다.")
    }
    return res.json();
  }).then(data => {
    const schools = data.schools;
    for (const school of schools) {
      const option = document.createElement("option");
      const schoolId = school.id;
      option.textContent = `${school.name} (ID=${schoolId})`;
      option.value = schoolId;
      schoolSelect.append(option);
    }
  })
}

fetchSchools();

function init() {
  const festivalCreateForm = document.getElementById("festivalCreateForm");
  festivalCreateForm.addEventListener("submit", createFestival);
}

function createFestival(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  const festivalData = {
    name: formData.get("name"),
    startDate: formData.get("startDate"),
    endDate: formData.get("endDate"),
    thumbnail: formData.get("thumbnail"),
    schoolId: formData.get("school"),
  };
  validateFestival(festivalData)

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
    alert("축제가 성공적으로 생성되었습니다!");
    location.reload();
  })
  .catch(error => {
    alert(error.message);
  });
}

init();
