package com.festago.festagostaff.presentation.ui.ticketvalidation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festagostaff.domain.repository.TicketRepository
import kotlinx.coroutines.launch

class TicketValidationViewModel(
    private val ticketRepository: TicketRepository,
) : ViewModel() {

    private val _ticketState: MutableLiveData<String> = MutableLiveData("")
    val ticketState: LiveData<String> = _ticketState

    private var latestCode = ""

    fun validateTicketCode(code: String) {
        viewModelScope.launch {
            _ticketState.value = ticketRepository.validateTicket(code).toString()
        }
        latestCode = code
    }

    fun clearLatestCode() {
        _ticketState.value = ""
        latestCode = ""
    }

    fun isLatestCode(code: String): Boolean = latestCode == code

    class TicketScanViewModelFactory(
        private val ticketRepository: TicketRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TicketValidationViewModel::class.java)) {
                return TicketValidationViewModel(ticketRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
