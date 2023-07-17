package com.festago.festagostaff.presentation.ui.ticketscan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TicketScanViewModel : ViewModel() {
    private val _ticketCode: MutableLiveData<String> = MutableLiveData("")
    val ticketCode: LiveData<String> = _ticketCode

    fun validateTicketCode(code: String) {
        _ticketCode.value = code
    }
}
