package com.festago.festago.presentation.ui.ticketreservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.ReservationRepository
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class TicketReservationViewModel(
    private val reservationRepository: ReservationRepository,
) : ViewModel() {
    private val _uiState = MutableSingleLiveData<TicketReservationUiState>()
    val uiState: SingleLiveData<TicketReservationUiState> = _uiState

    fun loadReservation() {
        viewModelScope.launch {
            _uiState.setValue(TicketReservationUiState.Loading)
            reservationRepository.loadReservation()
                .onSuccess {
                    _uiState.setValue(TicketReservationUiState.Success(it.toPresentation()))
                }.onFailure {
                    _uiState.setValue(TicketReservationUiState.Error)
                }
        }
    }

    companion object {
        class TicketReservationViewModelFactory(
            private val reservationRepository: ReservationRepository,
        ) : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TicketReservationViewModel::class.java)) {
                    return TicketReservationViewModel(reservationRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }
}
