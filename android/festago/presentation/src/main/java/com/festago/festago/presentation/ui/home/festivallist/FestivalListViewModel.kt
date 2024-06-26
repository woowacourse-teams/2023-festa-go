package com.festago.festago.presentation.ui.home.festivallist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.model.festival.FestivalFilter
import com.festago.festago.domain.model.festival.SchoolRegion
import com.festago.festago.domain.repository.FestivalRepository
import com.festago.festago.presentation.ui.home.festivallist.uistate.ArtistUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalFilterUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalListUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.PopularFestivalUiState
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
class FestivalListViewModel @Inject constructor(
    private val festivalRepository: FestivalRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FestivalListUiState>(FestivalListUiState.Loading)
    val uiState: StateFlow<FestivalListUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<FestivalListEvent>()
    val event: SharedFlow<FestivalListEvent> = _event.asSharedFlow()

    private var festivalFilter: FestivalFilter = FestivalFilter.PROGRESS

    fun initFestivalList(refresh: Boolean = false) {
        if (!refresh && uiState.value is FestivalListUiState.Success) return

        viewModelScope.launch {
            val schoolRegion = (uiState.value as? FestivalListUiState.Success)?.schoolRegion
            val deferredPopularFestivals = async { festivalRepository.loadPopularFestivals() }
            val deferredFestivals = async {
                festivalRepository.loadFestivals(
                    schoolRegion = schoolRegion,
                    festivalFilter = festivalFilter,
                )
            }
            runCatching {
                val festivalsPage = deferredFestivals.await().getOrThrow()
                val popularFestivals = deferredPopularFestivals.await().getOrThrow()

                _uiState.value = FestivalListUiState.Success(
                    PopularFestivalUiState(
                        title = popularFestivals.title,
                        festivals = popularFestivals.festivals.map { it.toUiState() },
                    ),
                    festivals = festivalsPage.festivals.map { it.toUiState() },
                    festivalFilter = festivalFilter.toUiState(),
                    isLastPage = festivalsPage.isLastPage,
                    schoolRegion = schoolRegion,
                )
            }.onFailure {
                handleFailure(key = KEY_INIT_FESTIVALS, throwable = it)
            }
        }
    }

    fun loadFestivals(
        festivalFilterUiState: FestivalFilterUiState? = null,
        schoolRegion: SchoolRegion? = null,
        isLoadMore: Boolean = false,
    ) {
        val successUiState = uiState.value as? FestivalListUiState.Success ?: return

        viewModelScope.launch {
            val currentFestivals = getCurrentFestivals(festivalFilterUiState)
            updateFestivalsState(festivalFilterUiState, successUiState)

            festivalRepository.loadFestivals(
                schoolRegion = schoolRegion,
                festivalFilter = festivalFilter,
                lastFestivalId = if (isLoadMore) {
                    currentFestivals.lastOrNull()?.id
                } else {
                    null
                },
                lastStartDate = if (isLoadMore) {
                    currentFestivals.lastOrNull()?.startDate
                } else {
                    null
                },
            ).onSuccess { festivalsPage ->
                _uiState.value = FestivalListUiState.Success(
                    successUiState.popularFestivalUiState,
                    festivals = if (isLoadMore) {
                        currentFestivals + festivalsPage.festivals.map { it.toUiState() }
                    } else {
                        festivalsPage.festivals.map { it.toUiState() }
                    },
                    festivalFilter = festivalFilter.toUiState(),
                    schoolRegion = schoolRegion,
                    isLastPage = festivalsPage.isLastPage,
                )
            }.onFailure {
                handleFailure(key = KEY_LOAD_FESTIVALS, throwable = it)
            }
        }
    }

    private fun handleFailure(key: String, throwable: Throwable) {
        _uiState.value = FestivalListUiState.Error {
            _uiState.value = FestivalListUiState.Loading
            initFestivalList()
        }
        analyticsHelper.logNetworkFailure(
            key = key,
            value = throwable.message.toString(),
        )
    }

    private fun updateFestivalsState(
        festivalFilterUiState: FestivalFilterUiState?,
        successUiState: FestivalListUiState.Success,
    ) {
        if (festivalFilterUiState == null) return
        _uiState.value = successUiState.copy(
            festivals = listOf(),
            isLastPage = false,
            festivalFilter = festivalFilterUiState,
        )
    }

    private fun getCurrentFestivals(festivalFilterUiState: FestivalFilterUiState?): List<FestivalItemUiState> {
        var festivals = (uiState.value as? FestivalListUiState.Success)?.festivals ?: listOf()

        if (festivalFilterUiState != null && festivalFilter != festivalFilterUiState.toDomain()) {
            festivalFilter = festivalFilterUiState.toDomain()
            festivals = listOf()
        }
        return festivals
    }

    private fun FestivalFilterUiState.toDomain() = when (this) {
        FestivalFilterUiState.PLANNED -> FestivalFilter.PLANNED
        FestivalFilterUiState.PROGRESS -> FestivalFilter.PROGRESS
    }

    private fun FestivalFilter.toUiState() = when (this) {
        FestivalFilter.PLANNED -> FestivalFilterUiState.PLANNED
        FestivalFilter.PROGRESS -> FestivalFilterUiState.PROGRESS
        else -> FestivalFilterUiState.PLANNED
    }

    private fun Festival.toUiState() = FestivalItemUiState(
        id = id,
        name = name,
        startDate = startDate,
        endDate = endDate,
        imageUrl = imageUrl,
        artists = artists.map { artist ->
            ArtistUiState(artist.id, artist.name, artist.imageUrl)
        },
        ::showFestivalDetail,
    )

    private fun showFestivalDetail(festival: FestivalItemUiState) {
        viewModelScope.launch {
            _event.emit(FestivalListEvent.ShowFestivalDetail(festival))
        }
    }

    companion object {
        private const val KEY_INIT_FESTIVALS = "KEY_INIT_FESTIVALS"
        private const val KEY_LOAD_FESTIVALS = "KEY_LOAD_FESTIVALS"
    }
}
