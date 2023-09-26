function init() {
  const serverVersionBtn = document.getElementById("serverVersionBtn");
  const infoLogBtn = document.getElementById("infoLogBtn");
  const warnLogBtn = document.getElementById("warnLogBtn");
  const errorLogBtn = document.getElementById("errorLogBtn");

  serverVersionBtn.addEventListener("click", showServerVersion);
  infoLogBtn.addEventListener("click", executeInfoLog);
  warnLogBtn.addEventListener("click", executeWarnLog);
  errorLogBtn.addEventListener("click", executeErrorLog);
}

function showServerVersion() {
  fetch("/admin/api/version")
  .then(res => {
    if (res.ok) {
      return res.text();
    }
    throw new Error("서버에 연결할 수 없습니다.");
  }).then(body => {
    alert(body);
  }).catch(error => {
    alert(error.message);
  })
}

function executeInfoLog() {
  fetch("/admin/api/info")
  .then(res => {
    if (res.status !== 400) {
      throw new Error("서버에 연결할 수 없습니다.");
    }
  }).then(() => {
    alert("실행 완료");
  }).catch(error => {
    alert(error.message);
  })
}

function executeWarnLog() {
  fetch("/admin/api/warn")
  .then(res => {
    if (res.status !== 500) {
      throw new Error("서버에 연결할 수 없습니다.");
    }
  }).then(() => {
    alert("실행 완료");
  }).catch(error => {
    alert(error.message);
  })
}

function executeErrorLog() {
  fetch("/admin/api/error")
  .then(res => {
    if (res.status !== 500) {
      throw new Error("서버에 연결할 수 없습니다.");
    }
  }).then(() => {
    alert("실행 완료");
  }).catch(error => {
    alert(error.message);
  })
}

init();
