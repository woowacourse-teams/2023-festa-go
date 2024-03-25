package com.festago.festago.presentation.ui.search.uistate

sealed interface SearchUiState {
    object Loading : SearchUiState

    data class RecentSearchSuccess(
        val recentSearchQueries: List<RecentSearchItemUiState>,
    ) : SearchUiState

    data class SearchSuccess(
        val searchedFestivals: List<FestivalSearchItemUiState>,
        val searchedArtists: List<ArtistSearchItemUiState>,
        val searchedSchools: List<SchoolSearchItemUiState>,
    ) : SearchUiState

    object Error : SearchUiState

    val shouldShowNotEmptyRecentSearchSuccess get() = this is RecentSearchSuccess && recentSearchQueries.isNotEmpty()
    val shouldShowEmptyRecentSearchSuccess get() = this is RecentSearchSuccess && recentSearchQueries.isEmpty()
    val shouldShowRecentSearchSuccess get() = this is RecentSearchSuccess
    val shouldShowSearchSuccess get() = this is SearchSuccess
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
