package com.festago.festago.presentation.ui.search.uistate

sealed interface SearchUiState {
    object Loading : SearchUiState

    data class RecentSearchSuccess(
        val recentSearchQueries: List<RecentSearchItemUiState>,
    ) : SearchUiState

    object Error : SearchUiState

    val shouldShowRecentSearchSuccessNotEmpty get() = this is SearchUiState.RecentSearchSuccess && recentSearchQueries.isNotEmpty()
    val shouldShowEmptyRecentSearchSuccessEmpty get() = this is SearchUiState.RecentSearchSuccess && recentSearchQueries.isEmpty()
    val shouldShowLoading get() = this is SearchUiState.Loading
    val shouldShowError get() = this is SearchUiState.Error
}
