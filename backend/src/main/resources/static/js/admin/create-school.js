const schoolGrid = document.getElementById("schoolGrid");
const schoolCreateForm = document.getElementById("schoolCreateForm");

function fetchSchools() {
  fetch("/schools").then(res => {
    if (!res.ok) {
      disableSelector("서버에 연결할 수 없습니다.");
      throw new Error("서버에 연결할 수 없습니다.")
    }
    return res.json();
  }).then(data => {
    const schools = data.schools;
    for (const school of schools) {
      const row = document.createElement("div");
      row.classList.add("row", "align-items-center", "gx-0", "py-1",
          "border-top");

      const idColumn = document.createElement("div");
      idColumn.classList.add("col-2");
      idColumn.textContent = school.id;

      const nameColumn = document.createElement("div");
      nameColumn.classList.add("col-4");
      nameColumn.textContent = school.name;

      const domainColumn = document.createElement("div");
      domainColumn.classList.add("col-4");
      domainColumn.textContent = school.domain;

      const buttonColumn = document.createElement("div");
      buttonColumn.classList.add("col-2")
      const button = document.createElement("a");
      button.classList.add("btn", "btn-primary");
      button.setAttribute("href", `modify-school?id=${school.id}`);
      button.textContent = "편집";
      buttonColumn.append(button);

      row.append(idColumn, nameColumn, domainColumn, buttonColumn);
      schoolGrid.append(row);
    }
  })
}

fetchSchools();

function init() {
  schoolCreateForm.addEventListener("submit", submitSchool);
}

function submitSchool(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  const schoolData = {
    name: formData.get("name"),
    domain: formData.get("domain"),
  };

  fetch("/admin/schools", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(schoolData)
  })
  .then(response => {
    if (response.ok) {
      return response.json();
    } else {
      return response.json().then(data => {
        throw new Error(data.message || "학교 생성에 실패하였습니다.");
      });
    }
  })
  .then(data => {
    alert("학교가 성공적으로 생성되었습니다!");
    location.reload();
  })
  .catch(error => {
    alert(error.message);
  });
}

init();
