export function validate(festivalData) {
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
