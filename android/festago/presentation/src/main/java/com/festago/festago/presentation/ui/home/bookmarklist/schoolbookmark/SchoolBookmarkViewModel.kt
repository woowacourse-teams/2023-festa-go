package com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.model.bookmark.SchoolBookmark
import com.festago.festago.domain.repository.BookmarkRepository
import com.festago.festago.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolBookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<SchoolBookmarkListUiState>(SchoolBookmarkListUiState.Loading)
    val uiState: StateFlow<SchoolBookmarkListUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SchoolBookmarkEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun fetchBookmarkList() {
        viewModelScope.launch {
            _uiState.value = SchoolBookmarkListUiState.Loading
            if (!userRepository.isSigned()) {
                _uiState.value = SchoolBookmarkListUiState.NotLoggedIn
                return@launch
            }
            bookmarkRepository.getSchoolBookmarks().onSuccess { schoolBookmarks ->
                _uiState.value = SchoolBookmarkListUiState.Success(
                    schoolBookmarks.map { it.toUiState() },
                )
            }.onFailure {
                _uiState.value = SchoolBookmarkListUiState.Error
            }
        }
    }

    fun logIn() {
        viewModelScope.launch {
            _uiEvent.emit(SchoolBookmarkEvent.ShowSignIn)
        }
    }

    private fun SchoolBookmark.toUiState(): SchoolBookmarkUiState {
        return SchoolBookmarkUiState(
            id = school.id,
            name = school.name,
            imageUrl = school.logoUrl,
            onSchoolDetail = { school ->
                viewModelScope.launch {
                    _uiEvent.emit(SchoolBookmarkEvent.ShowSchoolDetail(school))
                }
            },
        )
    }
}
