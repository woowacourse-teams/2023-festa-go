package com.festago.festago.presentation.ui.ticketentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.model.Ticket
import com.festago.festago.model.TicketCode
import com.festago.festago.model.timer.Timer
import com.festago.festago.model.timer.TimerListener
import com.festago.festago.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketEntryViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val ticketFlow = MutableSharedFlow<Result<Ticket>>()

    private val ticketCodeFlow = MutableSharedFlow<Result<TicketCode>>()

    private val _uiState: MutableStateFlow<TicketEntryUiState> =
        MutableStateFlow(TicketEntryUiState.Loading)
    val uiState: StateFlow<TicketEntryUiState> = _uiState.asStateFlow()

    private val timer: Timer = Timer()

    init {
        viewModelScope.launch {
            combine(ticketFlow, ticketCodeFlow) { ticketResult, ticketCodeResult ->
                runCatching {
                    val ticket = ticketResult.getOrThrowWithLog()
                    val ticketCode = ticketCodeResult.getOrThrowWithLog()

                    setTimer(ticket.id, ticketCode)

                    TicketEntryUiState.Success(
                        ticket = ticket,
                        ticketCode = ticketCode,
                        remainTime = ticketCode.period,
                    )
                }.getOrElse { TicketEntryUiState.Error }
            }.collectLatest { _uiState.value = it }
        }
    }

    fun loadTicketCode(ticketId: Long) {
        viewModelScope.launch {
            ticketCodeFlow.emit(ticketRepository.loadTicketCode(ticketId))
        }
    }

    fun loadTicket(ticketId: Long) {
        viewModelScope.launch {
            ticketFlow.emit(ticketRepository.loadTicket(ticketId))
        }
    }

    private fun setTimer(ticketId: Long, ticketCode: TicketCode) {
        viewModelScope.launch {
            timer.timerListener = createTimerListener(ticketId)
            timer.start(ticketCode.period)
        }
    }

    private fun createTimerListener(ticketId: Long): TimerListener = object : TimerListener {
        override fun onTick(current: Int) {
            val state = uiState.value
            if (state is TicketEntryUiState.Success) {
                _uiState.value = state.copy(remainTime = current)
            }
        }

        override fun onFinish() {
            loadTicketCode(ticketId)
        }
    }

    private fun Result<Ticket>.getOrThrowWithLog(): Ticket = getOrElse { throwable ->
        analyticsHelper.logNetworkFailure(
            key = KEY_LOAD_Ticket_LOG,
            value = throwable.message.toString(),
        )

        throw throwable
    }

    private fun Result<TicketCode>.getOrThrowWithLog(): TicketCode = getOrElse { throwable ->
        analyticsHelper.logNetworkFailure(
            key = KEY_LOAD_CODE_LOG,
            value = throwable.message.toString(),
        )
        throw throwable
    }

    companion object {
        private const val KEY_LOAD_Ticket_LOG = "load_ticket"
        private const val KEY_LOAD_CODE_LOG = "load_code"
    }
}
