package com.festago.festago.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.BookmarkRepository
import com.festago.festago.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val bookmarkRepository: BookmarkRepository,
) : ViewModel() {

    private val _event: MutableSharedFlow<SplashEvent> = MutableSharedFlow()
    val event: SharedFlow<SplashEvent> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            bookmarkRepository.getArtistBookmarks()
        }
        viewModelScope.launch {
            bookmarkRepository.getSchoolBookmarks()
        }
        viewModelScope.launch {
            bookmarkRepository.getFestivalBookmarkIds()
        }
    }

    fun checkSignIn() {
        viewModelScope.launch {
            when {
                userRepository.isSigned() -> _event.emit(SplashEvent.ShowHome)
                userRepository.isSignRejected() -> _event.emit(SplashEvent.ShowHome)
                else -> _event.emit(SplashEvent.ShowSignIn)
            }
        }
    }
}
