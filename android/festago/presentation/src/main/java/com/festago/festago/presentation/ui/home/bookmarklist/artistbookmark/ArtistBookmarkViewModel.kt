package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun fetchBookmarkList() {
        viewModelScope.launch {
            _uiState.value = ArtistBookmarkListUiState.Loading
            bookmarkRepository.getArtistBookmarks().onSuccess { artistBookmarks ->
                _uiState.value = ArtistBookmarkListUiState.Success(artistBookmarks)
            }.onFailure {
                _uiState.value = ArtistBookmarkListUiState.Error
            }
        }
    }
}
