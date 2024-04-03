package com.festago.festago.presentation.ui.search.uistate

data class RecentSearchItemUiState(
    val recentQuery: String,
    val onQuerySearched: (recentQuery: String) -> Unit,
    val onRecentSearchDeleted: (recentQuery: String) -> Unit,
)
