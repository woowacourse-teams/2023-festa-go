package com.festago.festago.presentation.ui.home

import androidx.lifecycle.ViewModel
import com.festago.festago.presentation.ui.home.HomeItemType.FESTIVAL_LIST
import com.festago.festago.presentation.ui.home.HomeItemType.MY_PAGE
import com.festago.festago.presentation.ui.home.HomeItemType.TICKET_LIST
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import com.festago.festago.repository.AuthRepository

class HomeViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _event = MutableSingleLiveData<HomeEvent>()
    val event: SingleLiveData<HomeEvent> = _event

    fun loadHomeItem(homeItemType: HomeItemType) {
        when {
            homeItemType == FESTIVAL_LIST -> _event.setValue(HomeEvent.ShowFestivalList)
            !authRepository.isSigned -> _event.setValue(HomeEvent.ShowSignIn)
            homeItemType == TICKET_LIST -> _event.setValue(HomeEvent.ShowTicketList)
            homeItemType == MY_PAGE -> _event.setValue(HomeEvent.ShowMyPage)
        }
    }
}
