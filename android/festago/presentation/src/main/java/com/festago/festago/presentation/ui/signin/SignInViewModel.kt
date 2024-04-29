package com.festago.festago.presentation.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _event: MutableSharedFlow<SignInEvent> = MutableSharedFlow()
    val event: SharedFlow<SignInEvent> = _event.asSharedFlow()

    fun signIn(idToken: String) {
        viewModelScope.launch {
            userRepository.signIn(idToken)
                .onSuccess {
                    _event.emit(SignInEvent.ShowHome)
                }.onFailure {
                    _event.emit(SignInEvent.SignInFailure)
                }
        }
    }

    fun rejectSignIn() {
        viewModelScope.launch {
            userRepository.rejectSignIn()
            _event.emit(SignInEvent.ShowHome)
        }
    }
}
