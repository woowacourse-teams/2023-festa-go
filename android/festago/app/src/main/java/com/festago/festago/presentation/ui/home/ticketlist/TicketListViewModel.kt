package com.festago.festago.presentation.ui.home.ticketlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import com.festago.festago.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketListViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TicketListUiState>(TicketListUiState.Loading)
    val uiState: StateFlow<TicketListUiState> = _uiState.asStateFlow()

    private val _event = MutableSingleLiveData<TicketListEvent>()
    val event: SingleLiveData<TicketListEvent> = _event

    fun loadCurrentTickets() {
        viewModelScope.launch {
            ticketRepository.loadCurrentTickets()
                .onSuccess { tickets ->
                    _uiState.value = TicketListUiState.Success(
                        tickets.map { TicketListItemUiState.of(it, ::showTicketEntry) },
                    )
                }.onFailure {
                    _uiState.value = TicketListUiState.Error
                    analyticsHelper.logNetworkFailure(KEY_LOAD_TICKETS_LOG, it.message.toString())
                }
        }
    }

    fun showTicketEntry(ticketId: Long) {
        _event.setValue(TicketListEvent.ShowTicketEntry(ticketId))
    }

    companion object {
        private const val KEY_LOAD_TICKETS_LOG = "load_tickets"
    }
}
