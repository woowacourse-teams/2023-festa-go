package com.festago.festago.presentation.ui.home.bookmarklist

import androidx.lifecycle.ViewModel
import com.festago.festago.domain.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BookmarkListViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<BookmarkListUiState>(BookmarkListUiState.Loading)
    val uiState: StateFlow<BookmarkListUiState> = _uiState.asStateFlow()
}
