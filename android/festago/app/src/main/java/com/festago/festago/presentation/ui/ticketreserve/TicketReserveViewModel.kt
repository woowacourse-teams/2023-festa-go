package com.festago.festago.presentation.ui.ticketreserve

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
    private val _uiState = MutableSingleLiveData<TicketReserveUiState>()
    val uiState: SingleLiveData<TicketReserveUiState> = _uiState

    fun loadReservation() {
        viewModelScope.launch {
            _uiState.setValue(TicketReserveUiState.Loading)
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
