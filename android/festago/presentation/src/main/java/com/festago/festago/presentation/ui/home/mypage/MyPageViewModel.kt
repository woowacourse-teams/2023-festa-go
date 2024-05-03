package com.festago.festago.presentation.ui.home.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<MyPageEvent> = MutableSharedFlow()
    val event: SharedFlow<MyPageEvent> = _event.asSharedFlow()

    fun loadUserInfo() {
        viewModelScope.launch {
            _uiState.value = MyPageUiState.Loading

            if (!userRepository.isSigned()) {
                _uiState.value = MyPageUiState.NotLoggedIn
                return@launch
            }

            userRepository.getUserInfo().onSuccess {
                _uiState.value = MyPageUiState.Success(it)
            }.onFailure {
                _uiState.value = MyPageUiState.Error(refresh = ::loadUserInfo)
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.ShowSignIn)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            if (!userRepository.isSigned()) return@launch

            userRepository.signOut().onSuccess {
                _event.emit(MyPageEvent.ShowSignIn)
            }.onFailure {
                _uiState.value = MyPageUiState.Error(refresh = ::loadUserInfo)
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            if (!userRepository.isSigned()) return@launch

            userRepository.deleteAccount().onSuccess {
                _event.emit(MyPageEvent.ShowSignIn)
            }.onFailure {
                _uiState.value = MyPageUiState.Error(refresh = ::loadUserInfo)
            }
        }
    }
}
