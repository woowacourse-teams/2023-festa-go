package com.festago.festago.presentation.ui.tickethistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketHistoryViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TicketHistoryUiState>(TicketHistoryUiState.Loading)
    val uiState: StateFlow<TicketHistoryUiState> = _uiState.asStateFlow()

    fun loadTicketHistories(size: Int = SIZE_TICKET_HISTORY, refresh: Boolean = false) {
        if (!refresh && uiState.value is TicketHistoryUiState.Success) return

        viewModelScope.launch {
            ticketRepository.loadHistoryTickets(size)
                .onSuccess { tickets ->
                    _uiState.value = TicketHistoryUiState.Success(
                        tickets.map { TicketHistoryItemUiState.from(it) },
                    )
                }.onFailure {
                    _uiState.value = TicketHistoryUiState.Error
                    analyticsHelper.logNetworkFailure(
                        key = KEY_LOAD_TICKET_HISTORIES_LOG,
                        value = it.message.toString(),
                    )
                }
        }
    }

    companion object {
        private const val KEY_LOAD_TICKET_HISTORIES_LOG = "ticket_histories"
        private const val SIZE_TICKET_HISTORY = 100
    }
}
