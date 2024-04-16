package com.festago.festago.presentation.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _event: MutableSharedFlow<SignInEvent> = MutableSharedFlow()
    val event: SharedFlow<SignInEvent> = _event.asSharedFlow()

    fun signIn(authCode: String) {
        viewModelScope.launch {
            authRepository.signIn(authCode)
                .onSuccess {
                    _event.emit(SignInEvent.ShowHome)
                }.onFailure {
                    _event.emit(SignInEvent.SignInFailure)
                }
        }
    }

    fun rejectSignIn() {
        viewModelScope.launch {
            authRepository.rejectSignIn()
            _event.emit(SignInEvent.ShowHome)
        }
    }
}
