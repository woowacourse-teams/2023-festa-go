package com.festago.festago.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.BookmarkRepository
import com.festago.festago.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    fun initBookmark() {
        viewModelScope.launch {
            if (!userRepository.isSigned()) return@launch

            bookmarkRepository.getArtistBookmarks().onSuccess {
                launch {
                    bookmarkRepository.getSchoolBookmarks()
                }
                launch {
                    bookmarkRepository.getFestivalBookmarkIds()
                }
            }
        }
    }
}
