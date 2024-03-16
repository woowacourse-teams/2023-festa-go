package com.festago.festago.presentation.ui.selectschool

interface SelectSchoolEvent {
    class ShowStudentVerification(val schoolId: Long) : SelectSchoolEvent
}
