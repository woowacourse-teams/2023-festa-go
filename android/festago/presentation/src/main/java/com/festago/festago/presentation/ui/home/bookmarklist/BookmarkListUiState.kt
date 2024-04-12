package com.festago.festago.presentation.ui.home.bookmarklist

interface BookmarkListUiState {
    object Loading : BookmarkListUiState

    data class Success(val value: Int) : BookmarkListUiState

    object Error : BookmarkListUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
