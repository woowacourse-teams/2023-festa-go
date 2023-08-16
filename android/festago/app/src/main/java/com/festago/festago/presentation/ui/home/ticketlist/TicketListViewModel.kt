package com.festago.festago.presentation.ui.home.ticketlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.domain.repository.TicketRepository
import com.festago.festago.presentation.mapper.toMemberTicketModel
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class TicketListViewModel(
    private val ticketRepository: TicketRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableLiveData<TicketListUiState>(TicketListUiState.Loading)
    val uiState: LiveData<TicketListUiState> = _uiState

    private val _event = MutableSingleLiveData<TicketListEvent>()
    val event: SingleLiveData<TicketListEvent> = _event

    fun loadCurrentTickets() {
        viewModelScope.launch {
            ticketRepository.loadCurrentTickets()
                .onSuccess { tickets ->
                    _uiState.value =
                        TicketListUiState.Success(tickets.toPresentation().toMemberTicketModel())
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
