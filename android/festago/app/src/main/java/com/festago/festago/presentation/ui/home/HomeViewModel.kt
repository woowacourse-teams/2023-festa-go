package com.festago.festago.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.presentation.ui.home.HomeItemType.FESTIVAL_LIST
import com.festago.festago.presentation.ui.home.HomeItemType.MY_PAGE
import com.festago.festago.presentation.ui.home.HomeItemType.TICKET_LIST
import com.festago.festago.repository.SocialAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val socialAuthRepository: SocialAuthRepository) : ViewModel() {

    private val _event = MutableSharedFlow<HomeEvent>()
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

    fun loadHomeItem(homeItemType: HomeItemType) {
        viewModelScope.launch {
            when {
                homeItemType == FESTIVAL_LIST -> _event.emit(HomeEvent.ShowFestivalList)
                !socialAuthRepository.isSigned -> _event.emit(HomeEvent.ShowSignIn)
                homeItemType == TICKET_LIST -> _event.emit(HomeEvent.ShowTicketList)
                homeItemType == MY_PAGE -> _event.emit(HomeEvent.ShowMyPage)
            }
        }
    }
}
