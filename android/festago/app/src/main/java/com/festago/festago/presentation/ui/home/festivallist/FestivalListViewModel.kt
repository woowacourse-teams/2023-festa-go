package com.festago.festago.presentation.ui.home.festivallist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.domain.repository.FestivalRepository
import com.festago.festago.presentation.ui.home.festivallist.FestivalListEvent.ShowTicketReserve
import com.festago.festago.presentation.util.MutableSingleLiveData
import com.festago.festago.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class FestivalListViewModel(
    private val festivalRepository: FestivalRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    private val _uiState = MutableLiveData<FestivalListUiState>(FestivalListUiState.Loading)
    val uiState: LiveData<FestivalListUiState> = _uiState

    private val _event = MutableSingleLiveData<FestivalListEvent>()
    val event: SingleLiveData<FestivalListEvent> = _event

    fun loadFestivals() {
        viewModelScope.launch {
            festivalRepository.loadFestivals()
                .onSuccess {
                    _uiState.value = FestivalListUiState.Success(
                        festivals = it.map { festival ->
                            FestivalItemUiState(
                                id = festival.id,
                                name = festival.name,
                                startDate = festival.startDate,
                                endDate = festival.endDate,
                                thumbnail = festival.thumbnail,
                                onFestivalDetail = ::showTicketReserve
                            )
                        }
                    )
                }.onFailure {
                    _uiState.value = FestivalListUiState.Error
                    analyticsHelper.logNetworkFailure(KEY_LOAD_FESTIVALS_LOG, it.message.toString())
                }
        }
    }

    fun showTicketReserve(festivalId: Long) {
        _event.setValue(ShowTicketReserve(festivalId))
    }

    companion object {
        private const val KEY_LOAD_FESTIVALS_LOG = "load_festivals"
    }
}
