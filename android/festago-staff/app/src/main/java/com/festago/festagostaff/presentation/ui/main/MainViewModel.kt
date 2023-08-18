package com.festago.festagostaff.presentation.ui.main

import androidx.lifecycle.ViewModel
import com.festago.festagostaff.util.MutableSingleLiveData
import com.festago.festagostaff.util.SingleLiveData

class MainViewModel : ViewModel() {
    private val _event = MutableSingleLiveData<MainEvent>()
    val event: SingleLiveData<MainEvent> get() = _event

    fun openTicketValidation() {
        _event.postValue(MainEvent.OpenTicketValidation)
    }
}
