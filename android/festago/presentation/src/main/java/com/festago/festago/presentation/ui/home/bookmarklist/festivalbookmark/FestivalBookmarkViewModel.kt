package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.model.artist.Artist
import com.festago.festago.domain.model.bookmark.FestivalBookmark
import com.festago.festago.domain.model.bookmark.FestivalBookmarkOrder
import com.festago.festago.domain.repository.BookmarkRepository
import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.artistadapter.ArtistUiState
import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.uistate.FestivalBookmarkItemUiState
import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.uistate.FestivalBookmarkUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FestivalBookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<FestivalBookmarkUiState>(FestivalBookmarkUiState.Loading)
    val uiState: StateFlow<FestivalBookmarkUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<FestivalBookmarkEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun fetchBookmarkList() {
        viewModelScope.launch {
            _uiState.value = FestivalBookmarkUiState.Loading
            bookmarkRepository.getFestivalBookmarkIds().onSuccess { bookmarkIds ->
                bookmarkRepository.getFestivalBookmarks(bookmarkIds, FestivalBookmarkOrder.FESTIVAL)
                    .onSuccess { festivalBookmarks ->
                        _uiState.value =
                            FestivalBookmarkUiState.Success(festivalBookmarks.map { it.toUiState() })
                    }.onFailure {
                        _uiState.value = FestivalBookmarkUiState.Error
                    }
            }.onFailure {
                _uiState.value = FestivalBookmarkUiState.Error
            }
        }
    }

    private fun FestivalBookmark.toUiState(): FestivalBookmarkItemUiState {
        return FestivalBookmarkItemUiState(
            id = festival.id,
            name = festival.name,
            imageUrl = festival.imageUrl,
            startDate = festival.startDate,
            endDate = festival.endDate,
            artists = festival.artists.map { it.toUiState() },
            onFestivalDetail = { festival ->
                viewModelScope.launch {
                    _uiEvent.emit(FestivalBookmarkEvent.ShowFestivalDetail(festival))
                }
            },
        )
    }

    private fun Artist.toUiState(): ArtistUiState {
        return ArtistUiState(
            id = id,
            name = name,
            imageUrl = imageUrl,
            onArtistDetail = { artist ->
                viewModelScope.launch {
                    _uiEvent.emit(FestivalBookmarkEvent.ShowArtistDetail(artist))
                }
            },
        )
    }
}
