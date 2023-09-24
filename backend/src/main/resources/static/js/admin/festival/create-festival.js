const schoolSelect = document.getElementById("schoolSelect");
const festivalForm = document.getElementById("festivalForm");

function fetchSchools() {
  fetch("/schools").then(res => {
    if (!res.ok) {
      disableSelector("서버에 연결할 수 없습니다.");
      throw new Error("서버에 연결할 수 없습니다.")
    }
    return res.json();
  }).then(data => {
    const schools = data.schools;
    if (schools.length === 0) {
      disableSelector("학교 목록이 없습니다.");
    }

    for (const school of schools) {
      const option = document.createElement("option");
      option.textContent = school.name;
      option.value = school.id;
      schoolSelect.append(option);
    }
  })
}

function disableSelector(reason) {
  schoolSelect.setAttribute("disabled", "");
  const schoolPlaceholder = document.getElementById("school-placeholder");
  schoolPlaceholder.textContent = reason;
}

fetchSchools();

function init() {
  festivalForm.addEventListener("submit", submitFestival);
}

function submitFestival(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  const festivalData = {
    name: formData.get("name"),
    startDate: formData.get("startDate"),
    endDate: formData.get("endDate"),
    thumbnail: formData.get("thumbnail"),
    schoolId: formData.get("school"),
  };
  validate(festivalData)

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

function validate(festivalData) {
  const startDate = new Date(festivalData.startDate);
  const endDate = new Date(festivalData.endDate);
  const now = new Date();
  let hasError = false;
  if (startDate < now) {
    document.getElementById("startDate").classList.add("is-invalid");
    document.getElementById("startDate-feedback")
        .textContent = "시작일은 현재 시간보다 뒤여야 합니다"
    hasError = true;
  }
  if (endDate < now) {
    document.getElementById("endDate").classList.add("is-invalid");
    document.getElementById("endDate-feedback")
        .textContent = "종료일은 현재 시간보다 뒤여야 합니다."
    hasError = true;
  }
  if (startDate > endDate) {
    document.getElementById("startDate").classList.add("is-invalid");
    document.getElementById("startDate-feedback")
        .textContent = "종료일은 시작일보다 뒤여야 합니다."
    hasError = true;
  }
  if (hasError) {
    throw new Error("검증이 실패하였습니다.");
  }
}

init();
