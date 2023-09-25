import {getResourceId} from "../../common/UrlParser.js";

function fetchSchool() {
  const idInput = document.getElementById("id");
  const fakeIdInput = document.getElementById("fakeId");
  const nameInput = document.getElementById("name");
  const domainInput = document.getElementById("domain");
  const updateBtn = document.getElementById("updateBtn");
  const deleteBtn = document.getElementById("deleteBtn");
  const schoolId = getResourceId(new URL(window.location.href));
  const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));

  fetch(`/schools/${schoolId}`).then(res => {
    if (!res.ok) {
      nameInput.setAttribute("disabled", "");
      domainInput.setAttribute("disabled", "");
      updateBtn.setAttribute("disabled", "");
      deleteBtn.setAttribute("disabled", "");
      return res.json().then(data => {
        throw new Error(data.message || data.detail)
      })
    }
    return res.json();
  }).then(school => {
    idInput.value = school.id;
    fakeIdInput.value = school.id;
    nameInput.value = school.name;
    domainInput.value = school.domain;
  }).catch(error => {
    const errorModalBody = document.getElementById("errorModalBody");
    errorModalBody.textContent = error.message;
    errorModal.show();
  })
}

fetchSchool();

function init() {
  const schoolUpdateForm = document.getElementById("schoolUpdateForm");
  const deleteBtn = document.getElementById("deleteBtn");
  const actualDeleteBtn = document.getElementById("actualDeleteBtn");

  schoolUpdateForm.addEventListener("submit", updateSchool);
  deleteBtn.addEventListener("click", openDeleteConfirmModal);
  actualDeleteBtn.addEventListener("click", deleteSchool)
}

function updateSchool(e) {
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
  const deleteConfirmModal = new bootstrap.Modal(
      document.getElementById("deleteConfirmModal"));
  deleteConfirmModal.show();
}

function deleteSchool() {
  const idInput = document.getElementById("id");
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
    location.replace("/admin/schools");
  })
  .catch(error => {
    alert(error.message);
  });
}
