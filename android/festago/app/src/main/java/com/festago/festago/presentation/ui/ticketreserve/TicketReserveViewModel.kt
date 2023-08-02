package com.festago.festago.presentation.ui.ticketreserve

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.ReservationRepository
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class TicketReserveViewModel(
    private val reservationRepository: ReservationRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData<TicketReserveUiState>()
    val uiState: LiveData<TicketReserveUiState> = _uiState

    private val _event = MutableSingleLiveData<TicketReserveEvent>()
    val event: SingleLiveData<TicketReserveEvent> = _event

    fun loadReservation(festivalId: Long = 0, refresh: Boolean = false) {
        if (!refresh && uiState.value is TicketReserveUiState.Success) return
        viewModelScope.launch {
            _uiState.value = TicketReserveUiState.Loading
            reservationRepository.loadReservation(festivalId)
                .onSuccess {
                    _uiState.setValue(TicketReserveUiState.Success(it.toPresentation()))
                }.onFailure {
                    _uiState.setValue(TicketReserveUiState.Error)
                }
        }
    }

    fun showTicketTypes(stageId: Int) {
        viewModelScope.launch {
            reservationRepository.reserveTicket(stageId)
                .onSuccess { tickets ->
                    _event.setValue(TicketReserveEvent.ShowTicketTypes(tickets.map { it.toPresentation() }))
                }.onFailure {
                    _uiState.setValue(TicketReserveUiState.Error)
                }
        }
    }

    fun reserveTicket() {
        viewModelScope.launch {
            reservationRepository.reserveTicket(0)
                .onSuccess {
                    _event.setValue(TicketReserveEvent.ReserveTicketSuccess)
                }.onFailure {
                    _event.setValue(TicketReserveEvent.ReserveTicketFailed)
                }
        }
    }

    companion object {
        class TicketReservationViewModelFactory(
            private val reservationRepository: ReservationRepository,
        ) : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TicketReserveViewModel::class.java)) {
                    return TicketReserveViewModel(reservationRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }
}
