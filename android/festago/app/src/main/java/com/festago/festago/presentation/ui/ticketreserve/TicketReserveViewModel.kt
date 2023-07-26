package com.festago.festago.presentation.ui.ticketreserve

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.ReservationRepository
import com.festago.festago.presentation.mapper.toPresentation
import kotlinx.coroutines.launch

class TicketReserveViewModel(
    private val reservationRepository: ReservationRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData<TicketReserveUiState>()
    val uiState: LiveData<TicketReserveUiState> = _uiState

    fun loadReservation(refresh: Boolean = false) {
        if (!refresh && uiState.value is TicketReserveUiState.Success) return
        viewModelScope.launch {
            _uiState.value = TicketReserveUiState.Loading
            reservationRepository.loadReservation()
                .onSuccess {
                    _uiState.setValue(TicketReserveUiState.Success(it.toPresentation()))
                }.onFailure {
                    _uiState.setValue(TicketReserveUiState.Error)
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
