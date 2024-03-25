package com.festago.festago.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.model.recentsearch.RecentSearchQuery
import com.festago.festago.domain.model.search.ArtistSearch
import com.festago.festago.domain.model.search.SchoolSearch
import com.festago.festago.domain.repository.RecentSearchRepository
import com.festago.festago.domain.repository.SearchRepository
import com.festago.festago.presentation.ui.search.screen.ItemSearchScreenUiState
import com.festago.festago.presentation.ui.search.screen.ItemSearchScreenUiState.FestivalSearchScreen
import com.festago.festago.presentation.ui.search.uistate.ArtistSearchItemUiState
import com.festago.festago.presentation.ui.search.uistate.ArtistUiState
import com.festago.festago.presentation.ui.search.uistate.FestivalSearchItemUiState
import com.festago.festago.presentation.ui.search.uistate.RecentSearchItemUiState
import com.festago.festago.presentation.ui.search.uistate.SchoolSearchItemUiState
import com.festago.festago.presentation.ui.search.uistate.SchoolUiState
import com.festago.festago.presentation.ui.search.uistate.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val recentSearchRepository: RecentSearchRepository,
    private val searchRepository: SearchRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SearchUiState> =
        MutableStateFlow(SearchUiState.RecentSearchSuccess(listOf()))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SearchEvent>()
    val event: SharedFlow<SearchEvent> = _event.asSharedFlow()

    var currentScreen: ItemSearchScreenUiState = FestivalSearchScreen(listOf())

    fun loadRecentSearch() {
        if (uiState.value is SearchUiState.SearchSuccess) return
        viewModelScope.launch {
            _uiState.value = SearchUiState.RecentSearchSuccess(
                recentSearchRepository.getRecentSearchQueries(10).map { it.toUiState() },
            )
        }
    }

    fun search(searchQuery: String) {
        viewModelScope.launch {
            if (searchQuery == "") {
                _event.emit(SearchEvent.SearchBlank)
                return@launch
            }
            _uiState.value = SearchUiState.Loading
            recentSearchRepository.insertOrReplaceRecentSearch(searchQuery)
            val deferredFestivals = async { searchRepository.searchFestivals(searchQuery) }
            val deferredArtist = async { searchRepository.searchArtists(searchQuery) }
            val deferredSchools = async { searchRepository.searchSchools(searchQuery) }
            runCatching {
                _uiState.value = SearchUiState.SearchSuccess(
                    deferredFestivals.await().getOrThrow().map { it.toUiState() },
                    deferredArtist.await().getOrThrow().map { it.toUiState() },
                    deferredSchools.await().getOrThrow().map { it.toUiState() },
                )
            }.onFailure {
                _uiState.value = SearchUiState.Error
                analyticsHelper.logNetworkFailure(
                    key = KEY_SEARCH,
                    value = it.message.toString(),
                )
            }
        }
    }

    private fun deleteRecentSearch(searchQuery: String) {
        viewModelScope.launch {
            recentSearchRepository.deleteRecentSearch(searchQuery)
            loadRecentSearch()
        }
    }

    fun deleteAllRecentSearch() {
        viewModelScope.launch {
            recentSearchRepository.clearRecentSearches()
            loadRecentSearch()
        }
    }

    private fun RecentSearchQuery.toUiState() = RecentSearchItemUiState(
        recentQuery = query,
        onQuerySearched = ::searchRecentQuery,
        onRecentSearchDeleted = ::deleteRecentSearch,
    )

    private fun Festival.toUiState() = FestivalSearchItemUiState(
        id = id,
        name = name,
        startDate = startDate,
        endDate = endDate,
        imageUrl = imageUrl,
        schoolUiState = SchoolUiState(
            id = school.id,
            name = school.name,
        ),
        artists = artists.map { artist ->
            ArtistUiState(artist.id, artist.name, artist.imageUrl, ::showArtistDetail)
        },
        ::showFestivalDetail,
    )

    private fun ArtistSearch.toUiState() = ArtistSearchItemUiState(
        id = id,
        name = name,
        profileImageUrl = profileImageUrl,
        todayStage = todayStage,
        upcomingStage = upcomingStage,
        onArtistDetailClick = ::showArtistDetail,
    )

    private fun SchoolSearch.toUiState() = SchoolSearchItemUiState(
        id = id,
        name = name,
        logoUrl = logoUrl,
        upcomingFestivalStartDate = upcomingFestivalStartDate,
        onSchoolSearchClick = ::showSchoolDetail,
    )

    private fun showFestivalDetail(festivalId: Long) {
        viewModelScope.launch {
            _event.emit(SearchEvent.ShowFestivalDetail(festivalId))
        }
    }

    private fun showArtistDetail(artistId: Long) {
        viewModelScope.launch {
            _event.emit(SearchEvent.ShowArtistDetail(artistId))
        }
    }

    private fun showSchoolDetail(schoolId: Long) {
        viewModelScope.launch {
            _event.emit(SearchEvent.ShowSchoolDetail(schoolId))
        }
    }

    private fun searchRecentQuery(searchQuery: String) {
        search(searchQuery)
        updateSearchQuery(searchQuery)
    }

    private fun updateSearchQuery(searchQuery: String) {
        viewModelScope.launch {
            _event.emit(SearchEvent.UpdateSearchQuery(searchQuery))
        }
    }

    companion object {
        private const val KEY_SEARCH = "KEY_SEARCH"
    }
}
