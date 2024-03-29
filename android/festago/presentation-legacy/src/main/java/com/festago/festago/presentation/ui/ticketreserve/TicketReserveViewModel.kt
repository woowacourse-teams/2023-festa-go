package com.festago.festago.presentation.ui.ticketreserve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.model.ErrorCode
import com.festago.festago.model.ReservationStage
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.FestivalRepository
import com.festago.festago.repository.ReservationTicketRepository
import com.festago.festago.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TicketReserveViewModel @Inject constructor(
    private val reservationTicketRepository: ReservationTicketRepository,
    private val festivalRepository: FestivalRepository,
    private val ticketRepository: TicketRepository,
    private val authRepository: AuthRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    private val _uiState = MutableStateFlow<TicketReserveUiState>(TicketReserveUiState.Loading)
    val uiState: StateFlow<TicketReserveUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<TicketReserveEvent>()
    val event: SharedFlow<TicketReserveEvent> = _event.asSharedFlow()

    fun loadReservation(festivalId: Long = 0, refresh: Boolean = false) {
        if (!refresh && uiState.value is TicketReserveUiState.Success) return
        viewModelScope.launch {
            festivalRepository.loadFestivalDetail(festivalId)
                .onSuccess {
                    _uiState.value = TicketReserveUiState.Success(
                        festival = ReservationFestivalUiState(
                            id = it.id,
                            name = it.name,
                            thumbnail = it.thumbnail,
                            endDate = it.endDate,
                            startDate = it.startDate,
                        ),
                        stages = it.reservationStages.toTicketReserveItems(),
                    )
                }.onFailure { error ->
                    _uiState.value = TicketReserveUiState.Error
                    analyticsHelper.logNetworkFailure(KEY_LOAD_RESERVATION_LOG, error.message ?: "")
                }
        }
    }

    fun showTicketTypes(stageId: Int, stageStartTime: LocalDateTime) {
        viewModelScope.launch {
            if (authRepository.isSigned) {
                reservationTicketRepository.loadTicketTypes(stageId)
                    .onSuccess { reservationTickets ->
                        _event.emit(
                            TicketReserveEvent.ShowTicketTypes(
                                stageStartTime,
                                reservationTickets.sortedByTicketTypes(),
                            ),
                        )
                    }.onFailure { error ->
                        _uiState.value = TicketReserveUiState.Error
                        analyticsHelper.logNetworkFailure(
                            KEY_SHOW_TICKET_TYPES_LOG,
                            error.message ?: "",
                        )
                    }
            } else {
                _event.emit(TicketReserveEvent.ShowSignIn)
            }
        }
    }

    fun reserveTicket(ticketId: Int) {
        viewModelScope.launch {
            ticketRepository.reserveTicket(ticketId)
                .onSuccess {
                    _event.emit(TicketReserveEvent.ReserveTicketSuccess(it))
                }.onFailure { error ->
                    if (error is ErrorCode) {
                        _event.emit(TicketReserveEvent.ReserveTicketFailed(error))
                    } else {
                        _event.emit(TicketReserveEvent.ReserveTicketFailed(ErrorCode.UNKNOWN()))
                        analyticsHelper.logNetworkFailure(KEY_RESERVE_TICKET, error.message ?: "")
                    }
                }
        }
    }

    private fun ReservationStage.toTicketReserveItem() = TicketReserveItemUiState(
        id = id,
        lineUp = lineUp,
        startTime = startTime,
        ticketOpenTime = ticketOpenTime,
        reservationTickets = reservationTickets.sortedByTicketTypes(),
        canReserve = LocalDateTime.now().isAfter(ticketOpenTime),
        isSigned = authRepository.isSigned,
        onShowStageTickets = ::showTicketTypes,
    )

    private fun List<ReservationStage>.toTicketReserveItems() = map {
        it.toTicketReserveItem()
    }

    companion object {

        private const val KEY_LOAD_RESERVATION_LOG = "load_reservation"
        private const val KEY_SHOW_TICKET_TYPES_LOG = "show_ticket_types"
        private const val KEY_RESERVE_TICKET = "reserve_ticket"
    }
}
