package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        MutableStateFlow<FestivalBookmarkListUiState>(FestivalBookmarkListUiState.Loading)
    val uiState: StateFlow<FestivalBookmarkListUiState> = _uiState.asStateFlow()

    fun fetchBookmarkList() {
        viewModelScope.launch {
            _uiState.value = FestivalBookmarkListUiState.Loading
            bookmarkRepository.getFestivalBookmarkIds().onSuccess { bookmarkIds ->
                bookmarkRepository.getFestivalBookmarks(bookmarkIds, FestivalBookmarkOrder.FESTIVAL)
                    .onSuccess { FestivalBookmarks ->
                        _uiState.value = FestivalBookmarkListUiState.Success(FestivalBookmarks)
                    }.onFailure {
                        _uiState.value = FestivalBookmarkListUiState.Error
                    }
            }.onFailure {
                _uiState.value = FestivalBookmarkListUiState.Error
            }
        }
    }
}
