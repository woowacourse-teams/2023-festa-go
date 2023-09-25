export function validateFestival(festivalData) {
  const startDate = new Date(festivalData.startDate);
  const endDate = new Date(festivalData.endDate);
  const now = new Date();
  let hasError = false;
  if (startDate < now) {
    document.getElementById("festivalStartDate").classList.add("is-invalid");
    document.getElementById("festivalStartDate-feedback")
        .textContent = "시작일은 현재 시간보다 이후 이어야 합니다"
    hasError = true;
  }
  if (endDate < now) {
    document.getElementById("festivalEndDate").classList.add("is-invalid");
    document.getElementById("festivalEndDate-feedback")
        .textContent = "종료일은 현재 시간보다 이후 이어야 합니다."
    hasError = true;
  }
  if (startDate > endDate) {
    document.getElementById("festivalStartDate").classList.add("is-invalid");
    document.getElementById("festivalStartDate-feedback")
        .textContent = "종료일은 시작일보다 이후 이어야 합니다."
    hasError = true;
  }
  if (hasError) {
    throw new Error("검증이 실패하였습니다.");
  }
}
