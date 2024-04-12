package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

import com.festago.festago.domain.model.bookmark.ArtistBookmark

interface ArtistBookmarkListUiState {
    object Loading : ArtistBookmarkListUiState

    data class Success(val artistBookmarks: List<ArtistBookmark>) : ArtistBookmarkListUiState

    object Error : ArtistBookmarkListUiState

    val shouldShowSuccess get() = this is Success && artistBookmarks.isNotEmpty()
    val shouldShowEmpty get() = this is Success && artistBookmarks.isEmpty()
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
