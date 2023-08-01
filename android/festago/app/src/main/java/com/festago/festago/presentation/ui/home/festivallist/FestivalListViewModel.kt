package com.festago.festago.presentation.ui.home.festivallist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.repository.FestivalRepository
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.ui.home.festivallist.FestivalListEvent.ShowTicketReserve
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class FestivalListViewModel(
    private val festivalRepository: FestivalRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData<FestivalListUiState>()
    val uiState: LiveData<FestivalListUiState> = _uiState

    private val _event = MutableSingleLiveData<FestivalListEvent>()
    val event: SingleLiveData<FestivalListEvent> = _event

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    }

    fun loadFestivals() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.value = FestivalListUiState.Loading
            festivalRepository.loadFestivals()
                .onSuccess {
                    _uiState.value = FestivalListUiState.Success(it.toPresentation())
                }.onFailure {
                    _uiState.value = FestivalListUiState.Error
                    throw it
                }
        }
    }

    fun showTicketReserve(festivalId: Long) {
        _event.setValue(ShowTicketReserve(festivalId))
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
