package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.model.bookmark.ArtistBookmark
import com.festago.festago.domain.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistBookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<ArtistBookmarkListUiState>(ArtistBookmarkListUiState.Loading)
    val uiState: StateFlow<ArtistBookmarkListUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ArtistBookmarkEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun fetchBookmarkList() {
        viewModelScope.launch {
            _uiState.value = ArtistBookmarkListUiState.Loading
            bookmarkRepository.getArtistBookmarks().onSuccess { artistBookmarks ->
                _uiState.value = ArtistBookmarkListUiState.Success(
                    artistBookmarks.map { it.toUiState() },
                )
            }.onFailure {
                _uiState.value = ArtistBookmarkListUiState.Error
            }
        }
    }

    private fun ArtistBookmark.toUiState(): ArtistBookmarkUiState {
        return ArtistBookmarkUiState(
            id = artist.id,
            name = artist.name,
            imageUrl = artist.profileImageUrl,
            onArtistDetail = { artist ->
                viewModelScope.launch {
                    _uiEvent.emit(ArtistBookmarkEvent.ShowArtistDetail(artist))
                }
            },
        )
    }
}
