package com.festago.festago.presentation.ui.artistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.repository.ArtistRepository
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistDetailUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.FestivalItemUiState
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
class ArtistDetailViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    private val _event: MutableSharedFlow<ArtistDetailEvent> = MutableSharedFlow()
    val event: SharedFlow<ArtistDetailEvent> = _event.asSharedFlow()

    private val _uiState: MutableStateFlow<ArtistDetailUiState> =
        MutableStateFlow(ArtistDetailUiState.Loading)
    val uiState: StateFlow<ArtistDetailUiState> = _uiState.asStateFlow()

    fun loadArtistDetail(id: Long, refresh: Boolean = false) {
        if (!refresh && _uiState.value is ArtistDetailUiState.Success) return

        viewModelScope.launch {
            runCatching {
                val deferredArtistDetail = async { artistRepository.loadArtistDetail(id) }
                val deferredFestivals = async { artistRepository.loadArtistFestivals(id, 10) }
                val festivalPage = deferredFestivals.await().getOrThrow()

                _uiState.value = ArtistDetailUiState.Success(
                    deferredArtistDetail.await().getOrThrow(),
                    festivalPage.toUiState(),
                    festivalPage.isLastPage
                )
            }.onFailure {
                handleFailure(key = KEY_LOAD_ARTIST_DETAIL, throwable = it)
            }
        }
    }

    fun loadMoreArtistFestivals(artistId: Long) {
        val successUiState = uiState.value as? ArtistDetailUiState.Success ?: return

        viewModelScope.launch {
            val currentFestivals = successUiState.festivals

            artistRepository.loadArtistFestivals(
                id = artistId,
                lastFestivalId = currentFestivals.lastOrNull()?.id,
                lastStartDate = currentFestivals.lastOrNull()?.startDate,
            ).onSuccess { festivalsPage ->
                _uiState.value = successUiState.copy(
                    festivals = currentFestivals + festivalsPage.toUiState(),
                    isLast = festivalsPage.isLastPage
                )
            }.onFailure {
                handleFailure(key = KEY_LOAD_MORE_ARTIST_FESTIVAL, throwable = it)
            }
        }
    }

    private fun handleFailure(key: String, throwable: Throwable) {
        _uiState.value = ArtistDetailUiState.Error {
            _uiState.value = ArtistDetailUiState.Loading
            loadArtistDetail(it)
        }
        analyticsHelper.logNetworkFailure(
            key = key,
            value = throwable.message.toString()
        )
    }

    private fun FestivalsPage.toUiState() = festivals.map {
        FestivalItemUiState(
            id = it.id,
            name = it.name,
            imageUrl = it.imageUrl,
            startDate = it.startDate,
            endDate = it.endDate,
            artists = it.artists.map { artist ->
                ArtistUiState(
                    id = artist.id,
                    name = artist.name,
                    imageUrl = artist.imageUrl,
                    onArtistDetailClick = { id ->
                        viewModelScope.launch {
                            _event.emit(ArtistDetailEvent.ShowArtistDetail(id))
                        }
                    },
                )
            },
            onFestivalDetailClick = { id ->
                viewModelScope.launch {
                    _event.emit(ArtistDetailEvent.ShowFestivalDetail(id))
                }
            },
        )
    }

    companion object {
        private const val KEY_LOAD_ARTIST_DETAIL = "KEY_LOAD_ARTIST_DETAIL"
        private const val KEY_LOAD_MORE_ARTIST_FESTIVAL = "KEY_LOAD_MORE_ARTIST_FESTIVAL"
    }
}
