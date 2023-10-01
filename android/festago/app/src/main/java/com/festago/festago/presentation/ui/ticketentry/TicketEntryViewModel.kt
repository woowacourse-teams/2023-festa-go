package com.festago.festago.presentation.ui.ticketentry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.model.TicketCode
import com.festago.festago.model.timer.Timer
import com.festago.festago.model.timer.TimerListener
import com.festago.festago.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketEntryViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableLiveData<TicketEntryUiState>()
    val uiState: LiveData<TicketEntryUiState> = _uiState

    private val timer: Timer = Timer()

    fun loadTicketCode(ticketId: Long) {
        viewModelScope.launch {
            ticketRepository.loadTicketCode(ticketId)
                .onSuccess {
                    val state = uiState.value
                    if (state is TicketEntryUiState.Success) {
                        _uiState.value = state.copy(ticketCode = it, remainTime = it.period)
                        setTimer(ticketId, it)
                    }
                }.onFailure {
                    _uiState.value = TicketEntryUiState.Error
                    analyticsHelper.logNetworkFailure(
                        key = KEY_LOAD_CODE_LOG,
                        value = it.message.toString(),
                    )
                }
        }
    }

    fun loadTicket(ticketId: Long) {
        viewModelScope.launch {
            _uiState.value = TicketEntryUiState.Loading
            ticketRepository.loadTicket(ticketId)
                .onSuccess { ticket ->
                    ticketRepository.loadTicketCode(ticketId)
                        .onSuccess { ticketCode ->
                            _uiState.value = TicketEntryUiState.Success(
                                ticket = ticket,
                                ticketCode = ticketCode,
                                remainTime = ticketCode.period,
                            )
                            setTimer(ticketId, ticketCode)
                        }.onFailure {
                            _uiState.value = TicketEntryUiState.Error
                            analyticsHelper.logNetworkFailure(
                                key = KEY_LOAD_CODE_LOG,
                                value = it.message.toString(),
                            )
                        }
                }.onFailure {
                    _uiState.value = TicketEntryUiState.Error
                    analyticsHelper.logNetworkFailure(
                        key = KEY_LOAD_Ticket_LOG,
                        value = it.message.toString(),
                    )
                }
        }
    }

    private suspend fun setTimer(ticketId: Long, ticketCode: TicketCode) {
        timer.timerListener = createTimerListener(
            ticketId = ticketId,
            period = ticketCode.period,
        )
        timer.start(ticketCode.period)
    }

    private fun createTimerListener(ticketId: Long, period: Int): TimerListener =
        object : TimerListener {
            override fun onTick(current: Int) {
                val state = uiState.value
                if (state is TicketEntryUiState.Success) {
                    _uiState.value = state.copy(remainTime = current)
                }
            }

            override fun onFinish() {
                viewModelScope.launch {
                    timer.start(period)
                    loadTicketCode(ticketId)
                }
            }
        }

    companion object {
        private const val KEY_LOAD_Ticket_LOG = "load_ticket"
        private const val KEY_LOAD_CODE_LOG = "load_code"
    }
}
