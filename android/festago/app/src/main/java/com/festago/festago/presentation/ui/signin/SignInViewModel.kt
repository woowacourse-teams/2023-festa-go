package com.festago.festago.presentation.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _event = MutableSharedFlow<SignInEvent>()
    val event: SharedFlow<SignInEvent> = _event

    private val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            _event.setValue(SignInEvent.SignInFailure)
            analyticsHelper.logNetworkFailure(KEY_SIGN_IN_LOG, throwable.message.toString())
        }

    fun signInKakao() {
        viewModelScope.launch {
            _event.emit(SignInEvent.ShowSignInPage)
        }
    }

    fun signIn(token: String) {
        viewModelScope.launch(exceptionHandler) {
            authRepository.signIn(SOCIAL_TYPE_KAKAO, token)
                .onSuccess {
                    _event.emit(SignInEvent.SignInSuccess)
                }.onFailure {
                    _event.emit(SignInEvent.SignInFailure)
                    analyticsHelper.logNetworkFailure(KEY_SIGN_IN_LOG, it.message.toString())
                }
        }
    }

    companion object {
        private const val SOCIAL_TYPE_KAKAO = "KAKAO"
        private const val KEY_SIGN_IN_LOG = "KEY_SIGN_IN_LOG"
    }
}
