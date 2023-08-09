package com.festago.festago.presentation.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.AuthRepository
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            withContext(Dispatchers.Default) {
                authRepository.signIn("KAKAO", token)
                    .onSuccess { token ->
                        authRepository.storeToken(token.accessToken)
                        _event.setValue(SignInEvent.SignInSuccess)
                    }.onFailure {
                        _event.setValue(SignInEvent.SignInFailure)
                    }
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
}
