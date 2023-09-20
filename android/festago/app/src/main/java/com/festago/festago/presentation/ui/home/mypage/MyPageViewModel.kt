package com.festago.festago.presentation.ui.home.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.TicketRepository
import com.festago.festago.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val ticketRepository: TicketRepository,
    private val authRepository: AuthRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableLiveData<MyPageUiState>(MyPageUiState.Loading)
    val uiState: LiveData<MyPageUiState> = _uiState

    private val _event = MutableSingleLiveData<MyPageEvent>()
    val event: SingleLiveData<MyPageEvent> = _event

    fun loadUserInfo() {
        if (!authRepository.isSigned) {
            _event.setValue(MyPageEvent.ShowSignIn)
            _uiState.value = MyPageUiState.Error
            return
        }
        viewModelScope.launch {
            val deferredUserProfile = async { userRepository.loadUserProfile() }
            val deferredHistoryTicket = async { ticketRepository.loadHistoryTickets(size = 1) }

            runCatching {
                _uiState.value = MyPageUiState.Success(
                    userProfile = deferredUserProfile.await().getOrThrow(),
                    ticket = deferredHistoryTicket.await().getOrThrow().firstOrNull(),
                )
            }.onFailure {
                _uiState.value = MyPageUiState.Error
                analyticsHelper.logNetworkFailure(
                    key = KEY_LOAD_USER_INFO,
                    value = it.message.toString(),
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
                .onSuccess {
                    _event.setValue(MyPageEvent.SignOutSuccess)
                    _uiState.value = MyPageUiState.Error
                }.onFailure {
                    _uiState.value = MyPageUiState.Error
                    analyticsHelper.logNetworkFailure(
                        key = KEY_SIGN_OUT,
                        value = it.message.toString(),
                    )
                }
        }
    }

    fun showConfirmDelete() {
        _event.setValue(MyPageEvent.ShowConfirmDelete)
    }

    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteAccount()
                .onSuccess {
                    _event.setValue(MyPageEvent.DeleteAccountSuccess)
                    _uiState.value = MyPageUiState.Error
                }.onFailure {
                    _uiState.value = MyPageUiState.Error
                    analyticsHelper.logNetworkFailure(
                        key = KEY_DELETE_ACCOUNT,
                        value = it.message.toString(),
                    )
                }
        }
    }

    fun showTicketHistory() {
        _event.setValue(MyPageEvent.ShowTicketHistory)
    }

    companion object {
        private const val KEY_LOAD_USER_INFO = "loadUserInfo"
        private const val KEY_SIGN_OUT = "KEY_SIGN_OUT"
        private const val KEY_DELETE_ACCOUNT = "KEY_DELETE_ACCOUNT"
    }
}
