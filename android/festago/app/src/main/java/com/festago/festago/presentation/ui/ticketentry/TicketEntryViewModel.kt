package com.festago.festago.presentation.ui.ticketentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.model.Ticket
import com.festago.festago.model.TicketCode
import com.festago.festago.model.timer.Timer
import com.festago.festago.model.timer.TimerListener
import com.festago.festago.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketEntryViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val ticketFlow =
        MutableStateFlow<Result<Ticket>>(Result.failure(Throwable("ticket not loaded")))

    private val ticketCodeFlow =
        MutableStateFlow<Result<TicketCode>>(Result.failure(Throwable("ticket code not loaded")))

    val uiState: StateFlow<TicketEntryUiState> =
        combine(
            ticketFlow,
            ticketCodeFlow,
        ) { ticketResult, ticketCodeResult ->
            val ticket = ticketResult.getOrElse { logTicketError(it) }
            val ticketCode = ticketCodeResult.getOrElse { logTicketCodeError(it) }
            if (ticket is Ticket && ticketCode is TicketCode) {
                TicketEntryUiState.Success(
                    ticket = ticket,
                    ticketCode = ticketCode,
                    remainTime = ticketCode.period,
                )
            } else {
                TicketEntryUiState.Error
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = TicketEntryUiState.Loading,
        )

    private val timer: Timer = Timer()

    fun loadTicketCode(ticketId: Long) {
        viewModelScope.launch {
            ticketCodeFlow.value = ticketRepository.loadTicketCode(ticketId)
            setTimer(ticketId, ticketCodeFlow.value.getOrThrow())
        }
    }

    fun loadTicket(ticketId: Long) {
        viewModelScope.launch {
            ticketFlow.value = ticketRepository.loadTicket(ticketId)
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
                    ticketCodeFlow.value = Result.success(state.ticketCode.copy(period = current))
                }
            }

            override fun onFinish() {
                viewModelScope.launch {
                    timer.start(period)
                    loadTicketCode(ticketId)
                }
            }
        }

    private fun logTicketError(exception: Throwable?) {
        analyticsHelper.logNetworkFailure(
            key = KEY_LOAD_Ticket_LOG,
            value = exception?.message.toString(),
        )
    }

    private fun logTicketCodeError(exception: Throwable) {
        analyticsHelper.logNetworkFailure(
            key = KEY_LOAD_CODE_LOG,
            value = exception.message.toString(),
        )
    }

    companion object {
        private const val KEY_LOAD_Ticket_LOG = "load_ticket"
        private const val KEY_LOAD_CODE_LOG = "load_code"
    }
}
