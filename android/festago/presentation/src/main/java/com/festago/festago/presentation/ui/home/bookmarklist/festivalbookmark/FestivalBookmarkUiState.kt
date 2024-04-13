package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

import com.festago.festago.domain.model.bookmark.FestivalBookmark

interface FestivalBookmarkUiState {
    object NotLoggedIn : FestivalBookmarkUiState
    object Loading : FestivalBookmarkUiState

    data class Success(val festivalBookmarks: List<FestivalBookmarkItemUiState>) : FestivalBookmarkUiState

    object Error : FestivalBookmarkUiState

    val shouldShowNotLoggedIn get() = this is NotLoggedIn

    val shouldShowSuccess get() = this is Success && festivalBookmarks.isNotEmpty()
    val shouldShowEmpty get() = this is Success && festivalBookmarks.isEmpty()
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
