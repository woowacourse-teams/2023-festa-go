import {getResourceId} from "../../common/UrlParser.js";

const deleteConfirmModal = new bootstrap.Modal(
    document.getElementById("deleteConfirmModal"));

function fetchStage() {
  const idInput = document.getElementById("id");
  const fakeIdInput = document.getElementById("fakeId");
  const festivalIdInput = document.getElementById("festivalId");
  const fakeFestivalIdInput = document.getElementById("fakeFestivalId");
  const startTimeInput = document.getElementById("startTime");
  const ticketOpenTime = document.getElementById("ticketOpenTime");
  const lineUpInput = document.getElementById("lineUp");
  const stageId = getResourceId(new URL(window.location.href));
  const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
  const returnLink = document.getElementById("returnLink");

  fetch(`/stages/${stageId}`).then(res => {
    if (!res.ok) {
      startTimeInput.setAttribute("disabled", "");
      ticketOpenTime.setAttribute("disabled", "");
      lineUpInput.setAttribute("disabled", "");
      return res.json().then(data => {
        throw new Error(data.message || data.detail)
      })
    }
    return res.json();
  }).then(stage => {
    idInput.value = stage.id;
    fakeIdInput.value = stage.id;
    festivalIdInput.value = stage.festivalId;
    fakeFestivalIdInput.value = stage.festivalId;
    startTimeInput.value = stage.startTime;
    ticketOpenTime.value = stage.ticketOpenTime;
    lineUpInput.value = stage.lineUp;
    returnLink.setAttribute("href", `/admin/festivals/${stage.festivalId}`)
  }).catch(error => {
    const errorModalBody = document.getElementById("errorModalBody");
    errorModalBody.textContent = error.message;
    errorModal.show();
  })
}

fetchStage()

function init() {
  const schoolUpdateForm = document.getElementById("schoolUpdateForm");
  const deleteBtn = document.getElementById("deleteBtn");
  const actualDeleteBtn = document.getElementById("actualDeleteBtn");

  schoolUpdateForm.addEventListener("submit", updateStage);
  deleteBtn.addEventListener("click", openDeleteConfirmModal);
  actualDeleteBtn.addEventListener("click", deleteStage)
}

function updateStage(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  const stageData = {
    startTime: formData.get("startTime"),
    ticketOpenTime: formData.get("ticketOpenTime"),
    lineUp: formData.get("lineUp"),
  };
  const stageId = formData.get("id");
  fetch(`/admin/api/stages/${stageId}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(stageData)
  })
  .then(response => {
    if (response.ok) {
      return response;
    } else {
      return response.json().then(data => {
        throw new Error(data.message || "공연 수정에 실패하였습니다.");
      });
    }
  })
  .then(() => {
    alert("공연이 성공적으로 수정되었습니다!");
    location.reload();
  })
  .catch(error => {
    alert(error.message);
  });
}

function openDeleteConfirmModal() {
  deleteConfirmModal.show();
}

function deleteStage() {
  const stageId = document.getElementById("id").value;
  const festivalId = document.getElementById("festivalId").value;
  fetch(`/admin/api/stages/${stageId}`, {
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
        throw new Error(data.message || "공연 삭제에 실패하였습니다.");
      });
    }
  })
  .then(() => {
    alert("공연이 성공적으로 삭제되었습니다!");
    location.replace(`/admin/festivals/${festivalId}`);
  })
  .catch(error => {
    deleteConfirmModal.hide();
    alert(error.message);
  });
}

init();
