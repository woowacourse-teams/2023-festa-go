package com.festago.festago.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.model.recentsearch.RecentSearchQuery
import com.festago.festago.domain.repository.RecentSearchRepository
import com.festago.festago.presentation.ui.search.uistate.RecentSearchItemUiState
import com.festago.festago.presentation.ui.search.uistate.SearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val recentSearchRepository: RecentSearchRepository,
) : ViewModel() {

    private var _uiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun loadRecentSearchQueries() {
        viewModelScope.launch {
            val recentSearchQueries = recentSearchRepository.getRecentSearchQueries(10)
            _uiState.value = SearchUiState.RecentSearchSuccess(
                recentSearchQueries.map { it.toUiState() },
            )
        }
    }

    fun search(searchQuery: String) {
        viewModelScope.launch {
            recentSearchRepository.insertOrReplaceRecentSearch(searchQuery)
        }
    }

    private fun deleteRecentSearch(searchQuery: String) {
        viewModelScope.launch {
            recentSearchRepository.deleteRecentSearch(searchQuery)
        }
    }

    private fun deleteAllRecentSearch() {
        viewModelScope.launch {
            recentSearchRepository.clearRecentSearches()
        }
    }

    private fun searchFestivals(searchQuery: String) {
    }

    private fun searchArtists(searchQuery: String) {
    }

    private fun searchSchool(searchQuery: String) {
    }

    private fun RecentSearchQuery.toUiState() = RecentSearchItemUiState(
        recentQuery = query,
        onQuerySearched = ::search,
        onRecentSearchDeleted = ::deleteRecentSearch,
    )
}
