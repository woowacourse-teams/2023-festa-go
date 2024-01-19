package com.festago.festago.presentation.ui.home.festivallist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FestivalListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<FestivalListUiState>(FestivalListUiState.Loading)
    val uiState: StateFlow<FestivalListUiState> = _uiState.asStateFlow()

    fun loadPopularFestival() {
        viewModelScope.launch {
            _uiState.value = FestivalListUiState.Success(
                popularFestivals = FakeFestivals.festivalList,
                festivals = FakeFestivals.festivalList,
            )
        }
    }
}
