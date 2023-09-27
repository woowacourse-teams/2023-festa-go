package com.festago.festago.presentation.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import com.festago.festago.repository.AuthRepository
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseMessaging: FirebaseMessaging,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _event = MutableSingleLiveData<SignInEvent>()
    val event: SingleLiveData<SignInEvent> = _event

    fun signInKakao() {
        _event.setValue(SignInEvent.ShowSignInPage)
    }

    fun signIn(token: String) {
        viewModelScope.launch {
            authRepository.signIn(
                SOCIAL_TYPE_KAKAO,
                token,
                firebaseMessaging.token.await(),
            ).onSuccess {
                _event.setValue(SignInEvent.SignInSuccess)
            }.onFailure {
                _event.setValue(SignInEvent.SignInFailure)
                analyticsHelper.logNetworkFailure(KEY_SIGN_IN_LOG, it.message.toString())
            }
        }
    }

    companion object {
        private const val SOCIAL_TYPE_KAKAO = "KAKAO"
        private const val KEY_SIGN_IN_LOG = "KEY_SIGN_IN_LOG"
    }
}
