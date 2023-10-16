package com.festago.festago.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _event = MutableSharedFlow<HomeEvent>()
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

    private val _selectedItem = MutableStateFlow(HomeItemType.FESTIVAL_LIST)
    val selectedItem: StateFlow<HomeItemType> = _selectedItem.asStateFlow()

    fun selectItem(homeItemType: HomeItemType) {
        when (homeItemType) {
            HomeItemType.FESTIVAL_LIST -> _selectedItem.value = homeItemType
            HomeItemType.TICKET_LIST -> selectItemOrSignIn(HomeItemType.TICKET_LIST)
            HomeItemType.MY_PAGE -> selectItemOrSignIn(HomeItemType.MY_PAGE)
        }
    }

    private fun selectItemOrSignIn(homeItemType: HomeItemType) {
        viewModelScope.launch {
            if (!authRepository.isSigned) {
                _event.emit(HomeEvent.ShowSignIn)
            } else {
                _selectedItem.emit(homeItemType)
            }
        }
    }
}
