import {validateFestival} from "./common-festival.js"
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
  const startDateInput = document.getElementById("festivalStartDate");
  const endDateInput = document.getElementById("festivalEndDate");
  const updateBtn = document.getElementById("festivalUpdateBtn");
  const deleteBtn = document.getElementById("festivalDeleteBtn");
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
  const deleteBtn = document.getElementById("festivalDeleteBtn");
  const actualDeleteBtn = document.getElementById("actualDeleteBtn");
  const stageCreateFrom = document.getElementById("stageCreateForm");

  festivalUpdateForm.addEventListener("submit", updateFestival);
  deleteBtn.addEventListener("click", openDeleteConfirmModal);
  actualDeleteBtn.addEventListener("click", deleteFestival);
  stageCreateFrom.addEventListener("submit", createStage);
}

function updateFestival(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  const festivalData = {
    name: formData.get("name"),
    startDate: formData.get("festivalStartDate"),
    endDate: formData.get("festivalEndDate"),
    thumbnail: formData.get("thumbnail"),
  };
  validateFestival(festivalData)
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
    button.setAttribute("href", `/admin/stages/${stage.id}`);
    button.textContent = "편집";
    buttonColumn.append(button);

    row.append(idColumn, startTimeColumn, ticketOpenTimeColumn, lineUpColumn,
        buttonColumn);
    stageGrid.append(row);
  }
}

function createStage(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  const stageData = {
    festivalId: document.getElementById("id").value,
    startTime: formData.get("stageStartTime"),
    ticketOpenTime: formData.get("ticketOpenTime"),
    lineUp: formData.get("lineUp"),
  };
  validateStage(stageData);

  fetch("/admin/api/stages", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(stageData)
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
    alert("공연이 성공적으로 생성되었습니다!");
    location.reload();
  })
  .catch(error => {
    alert(error.message);
  });
}

function validateStage(stageData) {
  const stageStartTime = new Date(stageData.startTime);
  const ticketOpenTime = new Date(stageData.ticketOpenTime);
  const now = new Date();
  let hasError = false;
  if (stageStartTime <= ticketOpenTime) {
    document.getElementById("ticketOpenTime").classList.add("is-invalid");
    document.getElementById("ticketOpenTime-feedback")
        .textContent = "티켓 오픈 시간은 공연 시작 이전 이어야 합니다."
    hasError = true;
  }
  if (stageStartTime < now) {
    document.getElementById("stageStartTime").classList.add("is-invalid");
    document.getElementById("stageStartTime-feedback")
        .textContent = "공연 시작 시간은 현재보다 이후 이어야 합니다."
    hasError = true;
  }
  if (ticketOpenTime < now) {
    document.getElementById("ticketOpenTime").classList.add("is-invalid");
    document.getElementById("ticketOpenTime-feedback")
        .textContent = "티켓 오픈 시간은 현재보다 이후 이어야 합니다."
    hasError = true;
  }
  if (hasError) {
    throw new Error("검증이 실패하였습니다.");
  }
}
