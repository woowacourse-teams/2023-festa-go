package com.festago.festago.presentation.ui.ticketreserve

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.domain.repository.ReservationRepository
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class TicketReserveViewModel(
    private val reservationRepository: ReservationRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    private val _uiState = MutableLiveData<TicketReserveUiState>(TicketReserveUiState.Loading)
    val uiState: LiveData<TicketReserveUiState> = _uiState

    private val _event = MutableSingleLiveData<TicketReserveEvent>()
    val event: SingleLiveData<TicketReserveEvent> = _event

    fun loadReservation(festivalId: Long = 0, refresh: Boolean = false) {
        if (!refresh && uiState.value is TicketReserveUiState.Success) return
        viewModelScope.launch {
            reservationRepository.loadReservation(festivalId)
                .onSuccess {
                    _uiState.setValue(TicketReserveUiState.Success(it.toPresentation()))
                }.onFailure {
                    _uiState.value = TicketReserveUiState.Error
                    analyticsHelper.logNetworkFailure(
                        KEY_LOAD_RESERVATION_LOG,
                        it.message.toString(),
                    )
                }
        }
    }

    fun showTicketTypes(stageId: Int) {
        viewModelScope.launch {
            reservationRepository.loadTicketTypes(stageId)
                .onSuccess { tickets ->
                    _event.setValue(
                        TicketReserveEvent.ShowTicketTypes(
                            stageId,
                            tickets.map { it.toPresentation() },
                        ),
                    )
                }.onFailure {
                    _uiState.setValue(TicketReserveUiState.Error)
                }
        }
    }

    fun reserveTicket(ticketId: Int) {
        viewModelScope.launch {
            reservationRepository.reserveTicket(ticketId)
                .onSuccess {
                    _event.setValue(TicketReserveEvent.ReserveTicketSuccess(it))
                }.onFailure {
                    _event.setValue(TicketReserveEvent.ReserveTicketFailed)
                }
        }
    }

    companion object {

        private const val KEY_LOAD_RESERVATION_LOG = "load_reservation"

        class TicketReservationViewModelFactory(
            private val reservationRepository: ReservationRepository,
            private val analyticsHelper: AnalyticsHelper,
        ) : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TicketReserveViewModel::class.java)) {
                    return TicketReserveViewModel(reservationRepository, analyticsHelper) as T
                }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }
}
