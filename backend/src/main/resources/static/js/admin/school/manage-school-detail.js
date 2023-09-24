const schoolUpdateForm = document.getElementById("schoolUpdateForm");
const idInput = document.getElementById("id");
const fakeIdInput = document.getElementById("fakeId");
const nameInput = document.getElementById("name");
const domainInput = document.getElementById("domain");
const updateBtn = document.getElementById("updateBtn");
const deleteBtn = document.getElementById("deleteBtn");
const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
const deleteConfirmModal = new bootstrap.Modal(
    document.getElementById("deleteConfirmModal"));
const errorModalBody = document.getElementById("errorModalBody");

function fetchSchool() {
  const currentUrl = new URL(window.location.href);
  const schoolId = currentUrl.searchParams.get("id");
  fetch(`/schools/${schoolId}`).then(res => {
    if (!res.ok) {
      nameInput.setAttribute("disabled", "");
      domainInput.setAttribute("disabled", "");
      updateBtn.setAttribute("disabled", "");
      deleteBtn.setAttribute("disabled", "");
      setModalBody(res.status);
      errorModal.show();
      throw new Error("서버에 연결할 수 없습니다.")
    }
    return res.json();
  }).then(school => {
    idInput.value = school.id;
    fakeIdInput.value = school.id;
    nameInput.value = school.name;
    domainInput.value = school.domain;
  })
}

function setModalBody(status) {
  if (status === 400) {
    errorModalBody.textContent = '잘못된 학교 ID 입니다.'
  }
  if (status === 404) {
    errorModalBody.textContent = '해당 학교가 존재하지 않습니다.'
  }
  if (status === 500) {
    errorModalBody.textContent = '서버에 연결할 수 없습니다.'
  }
}

fetchSchool();

function init() {
  schoolUpdateForm.addEventListener("submit", manageSchoolDetail);
}

function manageSchoolDetail(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  const schoolData = {
    name: formData.get("name"),
    domain: formData.get("domain"),
  };
  const schoolId = formData.get("id");
  fetch(`/admin/api/schools/${schoolId}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(schoolData)
  })
  .then(response => {
    if (response.ok) {
      return response;
    } else {
      return response.json().then(data => {
        throw new Error(data.message || "학교 수정에 실패하였습니다.");
      });
    }
  })
  .then(() => {
    alert("학교가 성공적으로 수정되었습니다!");
    location.reload();
  })
  .catch(error => {
    alert(error.message);
  });
}

init();

function openDeleteConfirmModal() {
  deleteConfirmModal.show();
}

function deleteSchool() {
  console.log("qqc")
  const schoolId = idInput.value;
  fetch(`/admin/api/schools/${schoolId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json"
    }
  })
  .then(response => {
    if (response.ok) {
      return response;
    } else {
      return response.json().then(data => {
        throw new Error(data.message || "학교 삭제에 실패하였습니다.");
      });
    }
  })
  .then(() => {
    alert("학교가 성공적으로 삭제되었습니다!");
    location.replace("/admin/create-school");
  })
  .catch(error => {
    alert(error.message);
  });
}
