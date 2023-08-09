package com.festago.festago.presentation.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.AuthRepository
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository,
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
                }
        }
    }

    class SignInViewModelFactory(
        private val socialSignRepository: AuthRepository,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
                return SignInViewModel(socialSignRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    companion object {
        private const val SOCIAL_TYPE_KAKAO = "KAKAO"
    }
}
