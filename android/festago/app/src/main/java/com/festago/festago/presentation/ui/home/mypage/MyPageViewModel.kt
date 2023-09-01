package com.festago.festago.presentation.ui.home.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.model.MemberTicketFestival
import com.festago.festago.model.Stage
import com.festago.festago.model.Ticket
import com.festago.festago.model.TicketCondition
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.TicketRepository
import com.festago.festago.repository.UserRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

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

    // TODO : 해당 프로퍼티 PR전에 제거
    val fakeTicket = Ticket(
        id = -1L,
        number = -1,
        entryTime = LocalDateTime.MIN,
        condition = TicketCondition.BEFORE_ENTRY,
        stage = Stage(
            id = -1,
            startTime = LocalDateTime.MIN,
        ),
        festivalTicket = MemberTicketFestival(
            id = -1,
            name = "",
            thumbnail = "",
        ),
        reserveAt = LocalDateTime.MIN,
    )

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
                    is MyPageUiState.Error,
                    is MyPageUiState.Loading,
                    null,
                    -> _uiState.value = MyPageUiState.Success(userProfile = it.toPresentation(), fakeTicket)

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
                val ticket = it.firstOrNull() ?: fakeTicket
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

    companion object {
        private const val KEY_LOAD_USER_INFO = "loadUserInfo"
        private const val KEY_SIGN_OUT = "KEY_SIGN_OUT"
        private const val KEY_DELETE_ACCOUNT = "KEY_DELETE_ACCOUNT"
    }
}
