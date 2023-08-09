package com.festago.festago.presentation.ui.home.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
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
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableLiveData<MyPageUiState>(MyPageUiState.Loading)
    val uiState: LiveData<MyPageUiState> = _uiState

    private val _event = MutableSingleLiveData<MyPageEvent>()
    val event: SingleLiveData<MyPageEvent> = _event

    fun loadUserInfo() {
        // TODO: 로그인 확인
        // TODO: _event.setValue(MyPageEvent.ShowLogin)

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

    class MyPageViewModelFactory(
        private val userRepository: UserRepository,
        private val ticketRepository: TicketRepository,
        private val analyticsHelper: AnalyticsHelper,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
                return MyPageViewModel(userRepository, ticketRepository, analyticsHelper) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    companion object {
        private const val KEY_LOAD_USER_INFO = "loadUserInfo"
    }
}
