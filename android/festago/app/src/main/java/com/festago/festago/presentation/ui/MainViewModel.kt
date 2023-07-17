package com.festago.festago.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.TicketRepository
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.model.TicketUiModel
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class MainViewModel(
    private val ticketRepository: TicketRepository,
) : ViewModel() {

    private val _event = MutableSingleLiveData<MainEvent>()
    val event: SingleLiveData<MainEvent> get() = _event

    private val _ticket = MutableSingleLiveData<TicketUiModel>()
    val ticket: SingleLiveData<TicketUiModel> get() = _ticket

    fun openTicketEntry() {
        when (val currentTicket = ticket.getValue()) {
            null -> null
            else -> _event.postValue(MainEvent.OpenTicketEntry(currentTicket))
        }
    }

    fun loadTicket() {
        viewModelScope.launch {
            _ticket.postValue(ticketRepository.loadTicket(0L).toPresentation())
        }
    }

    class MainViewModelFactory(
        private val ticketRepository: TicketRepository,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(ticketRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
