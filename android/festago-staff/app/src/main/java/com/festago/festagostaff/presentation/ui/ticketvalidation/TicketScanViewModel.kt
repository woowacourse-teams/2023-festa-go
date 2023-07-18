package com.festago.festagostaff.presentation.ui.ticketvalidation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festagostaff.domain.repository.TicketRepository
import kotlinx.coroutines.launch

class TicketScanViewModel(
    private val ticketRepository: TicketRepository,
) : ViewModel() {

    private val _ticketCode: MutableLiveData<String> = MutableLiveData("")
    val ticketCode: LiveData<String> = _ticketCode

    fun validateTicketCode(code: String) {
        viewModelScope.launch {
            _ticketCode.value = ticketRepository.validateTicket(code).state
        }
    }

    class TicketScanViewModelFactory(
        private val ticketRepository: TicketRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TicketScanViewModel::class.java)) {
                return TicketScanViewModel(ticketRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
