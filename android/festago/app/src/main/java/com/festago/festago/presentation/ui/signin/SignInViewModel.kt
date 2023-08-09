package com.festago.festago.presentation.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.domain.repository.AuthRepository
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _event = MutableSingleLiveData<SignInEvent>()
    val event: SingleLiveData<SignInEvent> = _event

    fun signInKakao() {
        _event.setValue(SignInEvent.ShowSignInPage)
    }

    fun signIn(token: String) {
        viewModelScope.launch {
            authRepository.signIn(SOCIAL_TYPE_KAKAO, token)
                .onSuccess {
                    _event.setValue(SignInEvent.SignInSuccess)
                }.onFailure {
                    _event.setValue(SignInEvent.SignInFailure)
                    analyticsHelper.logNetworkFailure(KEY_SIGN_IN_LOG, it.message.toString())
                }
        }
    }

    class SignInViewModelFactory(
        private val socialSignRepository: AuthRepository,
        private val analyticsHelper: AnalyticsHelper,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
                return SignInViewModel(socialSignRepository, analyticsHelper) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    companion object {
        private const val SOCIAL_TYPE_KAKAO = "KAKAO"
        private const val KEY_SIGN_IN_LOG = "KEY_SIGN_IN_LOG"
    }
}
