import {validate} from "./common-festival.js"
import {getResourceId} from "../../common/UrlParser.js";

const deleteConfirmModal = new bootstrap.Modal(
    document.getElementById("deleteConfirmModal"));

function fetchFestival() {
  const idInput = document.getElementById("id");
  const fakeIdInput = document.getElementById("fakeId");
  const schoolIdInput = document.getElementById("schoolId");
  const fakeSchoolIdInput = document.getElementById("fakeSchoolId");
  const nameInput = document.getElementById("name");
  const thumbnailInput = document.getElementById("thumbnail");
  const startDateInput = document.getElementById("startDate");
  const endDateInput = document.getElementById("endDate");
  const updateBtn = document.getElementById("updateBtn");
  const deleteBtn = document.getElementById("deleteBtn");
  const festivalId = getResourceId(new URL(window.location.href));
  const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));

  fetch(`/festivals/${festivalId}`).then(res => {
    if (!res.ok) {
      nameInput.setAttribute("disabled", "");
      thumbnailInput.setAttribute("disabled", "");
      startDateInput.setAttribute("disabled", "");
      endDateInput.setAttribute("disabled", "");
      updateBtn.setAttribute("disabled", "");
      deleteBtn.setAttribute("disabled", "");
      return res.json().then(data => {
        throw new Error(data.message || data.detail)
      })
    }
    return res.json();
  }).then(festival => {
    idInput.value = festival.id;
    fakeIdInput.value = festival.id;
    // TODO schoolIdInput.value = festival.schoolId
    // TODO fakeIdInput.value = festival.schoolId
    nameInput.value = festival.name;
    thumbnailInput.value = festival.thumbnail;
    startDateInput.value = festival.startDate;
    endDateInput.value = festival.endDate;
    renderStages(festival.stages)
  }).catch(error => {
    const errorModalBody = document.getElementById("errorModalBody");
    errorModalBody.textContent = error.message;
    errorModal.show();
  })
}

fetchFestival();

function init() {
  const festivalUpdateForm = document.getElementById("festivalUpdateForm");
  const deleteBtn = document.getElementById("deleteBtn");
  const actualDeleteBtn = document.getElementById("actualDeleteBtn");

  festivalUpdateForm.addEventListener("submit", updateFestival);
  deleteBtn.addEventListener("click", openDeleteConfirmModal)
  actualDeleteBtn.addEventListener("click", deleteFestival)
}

function updateFestival(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  const festivalData = {
    name: formData.get("name"),
    startDate: formData.get("startDate"),
    endDate: formData.get("endDate"),
    thumbnail: formData.get("thumbnail"),
  };
  validate(festivalData)
  const festivalId = formData.get("id");
  fetch(`/admin/api/festivals/${festivalId}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(festivalData)
  })
  .then(response => {
    if (response.ok) {
      return response;
    } else {
      return response.json().then(data => {
        throw new Error(data.message || "축제 수정에 실패하였습니다.");
      });
    }
  })
  .then(() => {
    alert("축제가 성공적으로 수정되었습니다!");
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

function deleteFestival() {
  const idInput = document.getElementById("id");
  const festivalId = idInput.value;
  fetch(`/admin/api/festivals/${festivalId}`, {
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
        throw new Error(data.message || "축제 삭제에 실패하였습니다.");
      });
    }
  })
  .then(() => {
    alert("축제가 성공적으로 삭제되었습니다!");
    location.replace("/admin/festivals");
  })
  .catch(error => {
    deleteConfirmModal.hide();
    alert(error.message);
  });
}

function renderStages(stages) {
  const stageGrid = document.getElementById("stageGrid");
  for (const stage of stages) {
    const row = document.createElement("div");
    row.classList.add("row", "align-items-center", "gx-0", "py-1",
        "border-top");

    const idColumn = document.createElement("div");
    idColumn.classList.add("col-1");
    idColumn.textContent = stage.id;

    const startTimeColumn = document.createElement("div");
    startTimeColumn.classList.add("col-3");
    startTimeColumn.textContent = stage.startTime;

    const ticketOpenTimeColumn = document.createElement("div");
    ticketOpenTimeColumn.classList.add("col-3");
    ticketOpenTimeColumn.textContent = stage.ticketOpenTime;

    const lineUpColumn = document.createElement("div");
    lineUpColumn.classList.add("col-3");
    lineUpColumn.textContent = stage.lineUp;

    const buttonColumn = document.createElement("div");
    buttonColumn.classList.add("col-2")
    const button = document.createElement("a");
    button.classList.add("btn", "btn-primary");
    button.setAttribute("href", `stages/${stage.id}`);
    button.textContent = "편집";
    buttonColumn.append(button);

    row.append(idColumn, startTimeColumn, ticketOpenTimeColumn, lineUpColumn,
        buttonColumn);
    stageGrid.append(row);
  }
}
