package com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark

sealed interface SchoolBookmarkEvent {
    class ShowSchoolDetail(val school: SchoolBookmarkUiState) : SchoolBookmarkEvent
    object ShowSignIn : SchoolBookmarkEvent
}
