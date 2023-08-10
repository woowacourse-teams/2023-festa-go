package com.festago.festago.presentation.ui.tickethistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.domain.repository.TicketRepository
import com.festago.festago.presentation.mapper.toPresentation
import kotlinx.coroutines.launch

class TicketHistoryViewModel(
    private val ticketRepository: TicketRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableLiveData<TicketHistoryUiState>()
    val uiState: LiveData<TicketHistoryUiState> = _uiState

    fun loadTicketHistories(size: Int = 100, refresh: Boolean = false) {
        if (!refresh && uiState.value is TicketHistoryUiState.Success) return

        viewModelScope.launch {
            _uiState.value = TicketHistoryUiState.Loading
            ticketRepository.loadHistoryTickets(size)
                .onSuccess { tickets ->
                    _uiState.value = TicketHistoryUiState.Success(tickets.toPresentation())
                }.onFailure {
                    _uiState.value = TicketHistoryUiState.Error
                    analyticsHelper.logNetworkFailure(
                        key = KEY_LOAD_Ticket_LOG,
                        value = it.message.toString(),
                    )
                }
        }
    }

    class TicketHistoryViewModelFactory(
        private val ticketRepository: TicketRepository,
        private val analyticsHelper: AnalyticsHelper,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TicketHistoryViewModel::class.java)) {
                return TicketHistoryViewModel(ticketRepository, analyticsHelper) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    companion object {
        private const val KEY_LOAD_Ticket_LOG = "load_ticket"
    }
}
