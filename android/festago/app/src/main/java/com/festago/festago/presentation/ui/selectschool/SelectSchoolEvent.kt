package com.festago.festago.presentation.ui.selectschool

interface SelectSchoolEvent {
    class ShowStudentVerification(schoolId: Long) : SelectSchoolEvent
}
