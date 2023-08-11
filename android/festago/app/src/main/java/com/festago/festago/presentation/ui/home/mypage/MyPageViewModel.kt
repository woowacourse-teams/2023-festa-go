package com.festago.festago.presentation.ui.home.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.domain.repository.AuthRepository
import com.festago.festago.domain.repository.TicketRepository
import com.festago.festago.domain.repository.UserRepository
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.model.TicketUiModel
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class MyPageViewModel(
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
            loadUserProfile()
            loadFirstTicket()
        }
    }

    private suspend fun loadUserProfile() {
        userRepository.loadUserProfile()
            .onSuccess {
                when (val current = uiState.value) {
                    is MyPageUiState.Error, null -> Unit

                    is MyPageUiState.Loading ->
                        _uiState.value = MyPageUiState.Success(userProfile = it.toPresentation())

                    is MyPageUiState.Success ->
                        _uiState.value = current.copy(userProfile = it.toPresentation())
                }
            }.onFailure {
                _uiState.value = MyPageUiState.Error
                analyticsHelper.logNetworkFailure(
                    key = KEY_LOAD_USER_INFO,
                    value = it.message.toString(),
                )
            }
    }

    private suspend fun loadFirstTicket() {
        ticketRepository.loadHistoryTickets(size = 1)
            .onSuccess {
                val ticket = it.firstOrNull()?.toPresentation() ?: TicketUiModel()
                when (val current = uiState.value) {
                    is MyPageUiState.Error, null -> Unit

                    is MyPageUiState.Loading ->
                        _uiState.value = MyPageUiState.Success(ticket = ticket)

                    is MyPageUiState.Success -> _uiState.value = current.copy(ticket = ticket)
                }
            }.onFailure {
                _uiState.value = MyPageUiState.Error
                analyticsHelper.logNetworkFailure(
                    key = KEY_LOAD_USER_INFO,
                    value = it.message.toString(),
                )
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

    class MyPageViewModelFactory(
        private val userRepository: UserRepository,
        private val ticketRepository: TicketRepository,
        private val authRepository: AuthRepository,
        private val analyticsHelper: AnalyticsHelper,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
                return MyPageViewModel(
                    userRepository = userRepository,
                    ticketRepository = ticketRepository,
                    authRepository = authRepository,
                    analyticsHelper = analyticsHelper,
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    companion object {
        private const val KEY_LOAD_USER_INFO = "loadUserInfo"
        private const val KEY_SIGN_OUT = "KEY_SIGN_OUT"
        private const val KEY_DELETE_ACCOUNT = "KEY_DELETE_ACCOUNT"
    }
}
