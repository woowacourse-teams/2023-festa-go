package com.festago.festago.presentation.ui.home.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            _uiState.value = MyPageUiState.Error(refresh = { refresh() })
        }
    }

    private fun refresh() {
        _uiState.value = MyPageUiState.Loading
        viewModelScope.launch {
            delay(1000)
            _uiState.value = MyPageUiState.Success
        }
    }
}
