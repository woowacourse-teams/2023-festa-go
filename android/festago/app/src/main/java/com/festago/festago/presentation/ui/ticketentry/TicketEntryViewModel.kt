package com.festago.festago.presentation.ui.ticketentry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.model.timer.Timer
import com.festago.festago.domain.model.timer.TimerListener
import com.festago.festago.domain.repository.TicketRepository
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.model.StageUiModel
import com.festago.festago.presentation.model.TicketCodeUiModel
import com.festago.festago.presentation.model.TicketStateUiModel
import com.festago.festago.presentation.model.TicketUiModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TicketEntryViewModel(
    private val ticketRepository: TicketRepository,
) : ViewModel() {
    private val _ticketRemainTime: MutableLiveData<Int> = MutableLiveData(DEFAULT_REMAIN_TIME)
    val ticketRemainTime: LiveData<Int> = _ticketRemainTime

    private val _ticketCode: MutableLiveData<TicketCodeUiModel> = MutableLiveData(
        DEFAULT_TICKET_CODE,
    )
    val ticketCode: LiveData<TicketCodeUiModel> = _ticketCode

    private val _ticket: MutableLiveData<TicketUiModel> = MutableLiveData(DEFAULT_TICKET)
    val ticket: LiveData<TicketUiModel> = _ticket

    private val timer: Timer = Timer()

    fun loadTicketCode(ticketId: Long) {
        viewModelScope.launch {
            val ticketCode = ticketRepository.loadTicketCode(ticketId)
            _ticketCode.value = ticketCode.toPresentation()

            timer.timerListener = createTimerListener(
                ticketId = ticketId,
                period = ticketCode.period,
            )
            timer.start(ticketCode.period)
        }
    }

    fun loadTicket(ticketId: Long) {
        viewModelScope.launch {
            val ticket = ticketRepository.loadTicket(ticketId)
            _ticket.value = ticket.toPresentation()
        }
    }

    private fun createTimerListener(ticketId: Long, period: Int): TimerListener =
        object : TimerListener {
            override fun onTick(current: Int) {
                _ticketRemainTime.value = current
            }

            override fun onFinish() {
                viewModelScope.launch {
                    timer.start(period)
                    loadTicketCode(ticketId)
                }
            }
        }

    class TicketEntryViewModelFactory(
        private val ticketRepository: TicketRepository,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TicketEntryViewModel::class.java)) {
                return TicketEntryViewModel(ticketRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    companion object {
        private const val DEFAULT_REMAIN_TIME = 0

        private val DEFAULT_TICKET_CODE = TicketCodeUiModel("festago.com", 0)

        private val DEFAULT_TICKET = TicketUiModel(
            id = 1,
            number = 123,
            entryTime = LocalDateTime.of(2023, 7, 17, 16, 0),
            ticketState = TicketStateUiModel.BEFORE_ENTRY,
            stage = StageUiModel(1, "페스타고", LocalDateTime.of(2023, 7, 17, 18, 0)),
        )
    }
}
