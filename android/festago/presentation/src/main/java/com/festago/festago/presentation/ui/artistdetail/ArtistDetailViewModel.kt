package com.festago.festago.presentation.ui.artistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.repository.ArtistRepository
import com.festago.festago.domain.repository.BookmarkRepository
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailEvent.FailedToFetchBookmarkList
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
    private val bookmarkRepository: BookmarkRepository,
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
                val deferredBookmarks = async { bookmarkRepository.getArtistBookmarks() }
                val artist = deferredArtistDetail.await().getOrThrow()
                val festivalPage = deferredFestivals.await().getOrThrow()
                val artistBookmarks = deferredBookmarks.await().getOrThrow()

                _uiState.value = ArtistDetailUiState.Success(
                    artist = artist,
                    bookMarked = artistBookmarks.firstOrNull { it.artist.id == artist.id.toLong() } != null,
                    festivals = festivalPage.toUiState(),
                    isLast = festivalPage.isLastPage,
                    onBookmarkClick = ::toggleArtistBookmark,
                )
            }.onFailure {
                _uiState.value = ArtistDetailUiState.Error
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
            }
        }
    }

    private fun toggleArtistBookmark(artistId: Int) {
        viewModelScope.launch {
            val uiState = uiState.value as? ArtistDetailUiState.Success ?: return@launch

            if (uiState.bookMarked) {
                bookmarkRepository.deleteArtistBookmark(artistId.toLong())
                    .onSuccess { _uiState.value = uiState.copy(bookMarked = false) }
                    .onFailure { _event.emit(FailedToFetchBookmarkList("최대 북마크 갯수를 초과했습니다")) }
            } else {
                bookmarkRepository.addArtistBookmark(artistId.toLong())
                    .onSuccess { _uiState.value = uiState.copy(bookMarked = true) }
                    .onFailure { _event.emit(FailedToFetchBookmarkList("최대 북마크 갯수를 초과했습니다")) }
            }
        }
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
}
