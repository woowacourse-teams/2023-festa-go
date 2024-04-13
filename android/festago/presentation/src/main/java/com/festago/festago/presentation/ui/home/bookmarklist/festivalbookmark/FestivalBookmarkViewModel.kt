package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.model.artist.Artist
import com.festago.festago.domain.model.bookmark.FestivalBookmark
import com.festago.festago.domain.model.bookmark.FestivalBookmarkOrder
import com.festago.festago.domain.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun fetchBookmarkList() {
        viewModelScope.launch {
            _uiState.value = FestivalBookmarkUiState.Loading
            bookmarkRepository.getFestivalBookmarkIds().onSuccess { bookmarkIds ->
                bookmarkRepository.getFestivalBookmarks(bookmarkIds, FestivalBookmarkOrder.FESTIVAL)
                    .onSuccess { FestivalBookmarks ->
                        _uiState.value =
                            FestivalBookmarkUiState.Success(FestivalBookmarks.map { it.toUiState() })
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
            onFestivalDetail = { festivalId ->
            },
        )
    }

    private fun Artist.toUiState(): ArtistUiState {
        return ArtistUiState(
            id = id,
            name = name,
            imageUrl = imageUrl,
        )
    }
}
