package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

interface ArtistBookmarkListUiState {
    object NotLoggedIn : ArtistBookmarkListUiState

    object Loading : ArtistBookmarkListUiState

    data class Success(val artistBookmarks: List<ArtistBookmarkUiState>) : ArtistBookmarkListUiState

    object Error : ArtistBookmarkListUiState

    val shouldShowNotLoggedIn get() = this is NotLoggedIn

    val shouldShowSuccess get() = this is Success && artistBookmarks.isNotEmpty()
    val shouldShowEmpty get() = this is Success && artistBookmarks.isEmpty()
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
