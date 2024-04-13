package com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark

interface SchoolBookmarkListUiState {
    object NotLoggedIn : SchoolBookmarkListUiState
    object Loading : SchoolBookmarkListUiState

    data class Success(val schoolBookmarks: List<SchoolBookmarkUiState>) : SchoolBookmarkListUiState

    object Error : SchoolBookmarkListUiState

    val shouldShowNotLoggedIn get() = this is NotLoggedIn

    val shouldShowSuccess get() = this is Success && schoolBookmarks.isNotEmpty()
    val shouldShowEmpty get() = this is Success && schoolBookmarks.isEmpty()
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
