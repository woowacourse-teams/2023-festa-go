export function validateFestival(festivalData) {
  const startDate = new Date(festivalData.startDate);
  const endDate = new Date(festivalData.endDate);
  let hasError = false;
  if (startDate > endDate) {
    document.getElementById("festivalEndDate").classList.add("is-invalid");
    document.getElementById("festivalEndDate-feedback")
        .textContent = "종료일은 시작일보다 이후 이어야 합니다."
    hasError = true;
  }
  if (hasError) {
    throw new Error("검증이 실패하였습니다.");
  }
}
