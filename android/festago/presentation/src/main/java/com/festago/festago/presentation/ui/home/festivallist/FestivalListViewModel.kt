package com.festago.festago.presentation.ui.home.festivallist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.repository.FestivalRepository
import com.festago.festago.presentation.ui.home.festivallist.uistate.ArtistUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalListUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.SchoolUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FestivalListViewModel @Inject constructor(
    private val festivalRepository: FestivalRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FestivalListUiState>(FestivalListUiState.Loading)
    val uiState: StateFlow<FestivalListUiState> = _uiState.asStateFlow()

    fun loadFestivals() {
        viewModelScope.launch {
            val deferredPopularFestivals = async { festivalRepository.loadPopularFestivals() }
            val deferredFestivals = async { festivalRepository.loadFestivals() }

            runCatching {
                val festivalsPage = deferredFestivals.await().getOrThrow()
                val popularFestivals = deferredPopularFestivals.await().getOrThrow()

                _uiState.value = FestivalListUiState.Success(
                    popularFestivals = popularFestivals.map { it.toUiState() },
                    festivals = festivalsPage.festivals.map { it.toUiState() },
                    isLastPage = festivalsPage.isLastPage,
                )
            }.onFailure {
                _uiState.value = FestivalListUiState.Error
                analyticsHelper.logNetworkFailure(
                    key = KEY_LOAD_FESTIVAL,
                    value = it.message.toString(),
                )
            }
        }
    }

    private fun Festival.toUiState() = FestivalItemUiState(
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
            ArtistUiState(artist.id, artist.name, artist.imageUrl)
        },
    )

    companion object {
        private const val KEY_LOAD_FESTIVAL = "KEY_LOAD_FESTIVAL"
    }
}
