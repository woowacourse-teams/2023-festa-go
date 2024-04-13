package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

import com.festago.festago.domain.model.bookmark.FestivalBookmark

interface FestivalBookmarkListUiState {
    object NotLoggedIn : FestivalBookmarkListUiState
    object Loading : FestivalBookmarkListUiState

    data class Success(val FestivalBookmarks: List<FestivalBookmark>) : FestivalBookmarkListUiState

    object Error : FestivalBookmarkListUiState

    val shouldShowNotLoggedIn get() = this is NotLoggedIn

    val shouldShowSuccess get() = this is Success && FestivalBookmarks.isNotEmpty()
    val shouldShowEmpty get() = this is Success && FestivalBookmarks.isEmpty()
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
