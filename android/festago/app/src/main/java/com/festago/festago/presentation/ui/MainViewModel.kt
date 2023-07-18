package com.festago.festago.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.TicketRepository
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.model.TicketUiModel
import com.festago.festago.presentation.ui.MainEvent.FailToLoadTicket
import com.festago.festago.presentation.ui.MainEvent.FailToOpenTicketEntry
import com.festago.festago.presentation.ui.MainEvent.OpenTicketEntry
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainViewModel(
    private val ticketRepository: TicketRepository,
) : ViewModel() {

    private val _event = MutableSingleLiveData<MainEvent>()
    val event: SingleLiveData<MainEvent> get() = _event

    private val _ticket = MutableLiveData<TicketUiModel>()
    val ticket: LiveData<TicketUiModel> get() = _ticket

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _event.postValue(FailToLoadTicket)
    }

    fun openTicketEntry() {
        ticket.value?.let { _event.postValue(OpenTicketEntry(it)) }
            ?: _event.postValue(FailToOpenTicketEntry)
    }

    fun loadTicket() {
        viewModelScope.launch(exceptionHandler) {
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
