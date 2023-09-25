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

fetchStage();

function fetchTickets() {
  const ticketGrid = document.getElementById("ticketGrid");
  const stageId = getResourceId(new URL(window.location.href));
  fetch(`/stages/${stageId}/tickets`).then(res => {
    if (!res.ok) {
      throw new Error("서버에 연결할 수 없습니다.")
    }
    return res.json();
  }).then(data => {
    const tickets = data.tickets;
    for (const ticket of tickets) {
      const row = document.createElement("div");
      row.classList.add("row", "align-items-center", "gx-0", "py-1",
          "border-top");

      const idColumn = document.createElement("div");
      idColumn.classList.add("col-1");
      idColumn.textContent = ticket.id;

      const ticketTypeColumn = document.createElement("div");
      ticketTypeColumn.classList.add("col-3");
      ticketTypeColumn.textContent = ticket.ticketType;

      const totalAmountColumn = document.createElement("div");
      totalAmountColumn.classList.add("col-3");
      totalAmountColumn.textContent = ticket.totalAmount;

      const remainAmountColumn = document.createElement("div");
      remainAmountColumn.classList.add("col-3");
      remainAmountColumn.textContent = ticket.remainAmount;

      const buttonColumn = document.createElement("div");
      buttonColumn.classList.add("col-2")
      const button = document.createElement("a");
      button.classList.add("btn", "btn-primary");
      button.setAttribute("href", `/admin/tickets/${ticket.id}`);
      button.textContent = "편집";
      buttonColumn.append(button);

      row.append(idColumn, ticketTypeColumn, totalAmountColumn,
          remainAmountColumn, buttonColumn);
      ticketGrid.append(row);
    }
  })
}

fetchTickets();

function init() {
  const schoolUpdateForm = document.getElementById("schoolUpdateForm");
  const ticketCreateForm = document.getElementById("ticketCreateForm");
  const deleteBtn = document.getElementById("deleteBtn");
  const actualDeleteBtn = document.getElementById("actualDeleteBtn");

  schoolUpdateForm.addEventListener("submit", updateStage);
  deleteBtn.addEventListener("click", openDeleteConfirmModal);
  actualDeleteBtn.addEventListener("click", deleteStage)
  ticketCreateForm.addEventListener("submit", createTicket)
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

function createTicket(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  const ticketData = {
    stageId: document.getElementById("id").value,
    ticketType: formData.get("ticketType"),
    amount: formData.get("amount"),
    entryTime: formData.get("entryTime"),
  };
  validateTicket(ticketData);

  fetch("/admin/api/tickets", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(ticketData)
  })
  .then(response => {
    if (response.ok) {
      return response;
    } else {
      return response.json().then(data => {
        throw new Error(data.message || "티켓 생성에 실패하였습니다.");
      });
    }
  })
  .then(() => {
    alert("티켓이 성공적으로 생성되었습니다!");
    location.reload();
  })
  .catch(error => {
    alert(error.message);
  });
}

function validateTicket(ticketData) {
  let hasError = false;
  if (ticketData.amount <= 0) {
    document.getElementById("amount").classList.add("is-invalid");
    document.getElementById("amount-feedback")
        .textContent = "수량은 0보다 많아야 합니다."
    hasError = true;
  }
  if (ticketData.amount > 100000) {
    document.getElementById("amount").classList.add("is-invalid");
    document.getElementById("amount-feedback")
        .textContent = "너무 많은 수량은 생성할 수 없습니다."
    hasError = true;
  }
  if (hasError) {
    throw new Error("검증이 실패하였습니다.");
  }
}

init();
