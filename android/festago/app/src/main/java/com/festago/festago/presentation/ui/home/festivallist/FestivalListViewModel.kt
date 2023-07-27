package com.festago.festago.presentation.ui.home.festivallist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.FestivalRepository
import com.festago.festago.presentation.mapper.toPresentation
import kotlinx.coroutines.launch

class FestivalListViewModel(
    private val festivalRepository: FestivalRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData<FestivalListUiState>()
    val uiState: LiveData<FestivalListUiState> = _uiState

    fun loadFestivals() {
        viewModelScope.launch {
            _uiState.value = FestivalListUiState.Loading
            festivalRepository.loadFestivals()
                .onSuccess {
                    _uiState.value = FestivalListUiState.Success(it.toPresentation())
                }.onFailure {
                    _uiState.value = FestivalListUiState.Error
                }
        }
    }

    class FestivalListViewModelFactory(
        private val festivalRepository: FestivalRepository,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FestivalListViewModel::class.java)) {
                return FestivalListViewModel(festivalRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
