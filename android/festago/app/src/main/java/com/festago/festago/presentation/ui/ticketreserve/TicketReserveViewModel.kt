package com.festago.festago.presentation.ui.ticketreserve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.model.ReservationStage
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.FestivalRepository
import com.festago.festago.repository.ReservationTicketRepository
import com.festago.festago.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _event = MutableSingleLiveData<TicketReserveEvent>()
    val event: SingleLiveData<TicketReserveEvent> = _event

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
                }.onFailure {
                    _uiState.value = TicketReserveUiState.Error
                    analyticsHelper.logNetworkFailure(
                        KEY_LOAD_RESERVATION_LOG,
                        it.message.toString(),
                    )
                }
        }
    }

    fun showTicketTypes(stageId: Int, stageStartTime: LocalDateTime) {
        viewModelScope.launch {
            if (authRepository.isSigned) {
                reservationTicketRepository.loadTicketTypes(stageId)
                    .onSuccess { tickets ->
                        _event.setValue(
                            TicketReserveEvent.ShowTicketTypes(
                                stageStartTime,
                                tickets,
                            ),
                        )
                    }.onFailure {
                        _uiState.value = TicketReserveUiState.Error
                    }
            } else {
                _event.setValue(TicketReserveEvent.ShowSignIn)
            }
        }
    }

    fun reserveTicket(ticketId: Int) {
        viewModelScope.launch {
            ticketRepository.reserveTicket(ticketId)
                .onSuccess {
                    _event.setValue(TicketReserveEvent.ReserveTicketSuccess(it))
                }.onFailure {
                    _event.setValue(TicketReserveEvent.ReserveTicketFailed)
                }
        }
    }

    private fun ReservationStage.toTicketReserveItem() = TicketReserveItemUiState(
        id = id,
        lineUp = lineUp,
        startTime = startTime,
        ticketOpenTime = ticketOpenTime,
        reservationTickets = reservationTickets,
        canReserve = LocalDateTime.now().isAfter(ticketOpenTime),
        isSigned = authRepository.isSigned,
        onShowStageTickets = ::showTicketTypes,
    )

    private fun List<ReservationStage>.toTicketReserveItems() = map {
        it.toTicketReserveItem()
    }

    companion object {

        private const val KEY_LOAD_RESERVATION_LOG = "load_reservation"
    }
}
