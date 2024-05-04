package com.festago.festago.presentation.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
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
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _event: MutableSharedFlow<SignInEvent> = MutableSharedFlow()
    val event: SharedFlow<SignInEvent> = _event.asSharedFlow()

    fun signIn(idToken: String) {
        viewModelScope.launch {
            userRepository.signIn(idToken)
                .onSuccess {
                    _event.emit(SignInEvent.SignInSuccess)
                }.onFailure {
                    _event.emit(SignInEvent.SignInFailure)
                    analyticsHelper.logNetworkFailure(
                        key = KEY_SIGN_IN,
                        value = it.message.toString(),
                    )
                }
        }
    }

    fun rejectSignIn() {
        viewModelScope.launch {
            userRepository.rejectSignIn()
            _event.emit(SignInEvent.ShowHome)
        }
    }

    companion object {
        private const val KEY_SIGN_IN = "KEY_SIGN_IN"
    }
}
