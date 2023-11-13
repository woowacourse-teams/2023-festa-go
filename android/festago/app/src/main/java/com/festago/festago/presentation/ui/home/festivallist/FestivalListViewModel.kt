package com.festago.festago.presentation.ui.home.festivallist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.model.FestivalFilter
import com.festago.festago.presentation.ui.home.festivallist.FestivalListEvent.ShowTicketReserve
import com.festago.festago.repository.FestivalRepository
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
class FestivalListViewModel @Inject constructor(
    private val festivalRepository: FestivalRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FestivalListUiState>(FestivalListUiState.Loading)
    val uiState: StateFlow<FestivalListUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<FestivalListEvent>()
    val event: SharedFlow<FestivalListEvent> = _event.asSharedFlow()

    fun loadFestivals(festivalFilter: FestivalFilter) {
        viewModelScope.launch {
            festivalRepository.loadFestivals(festivalFilter)
                .onSuccess {
                    _uiState.value = FestivalListUiState.Success(
                        festivals = it.map { festival ->
                            FestivalItemUiState(
                                id = festival.id,
                                name = festival.name,
                                startDate = festival.startDate,
                                endDate = festival.endDate,
                                thumbnail = festival.thumbnail,
                                onFestivalDetail = ::showTicketReserve,
                            )
                        },
                    )
                }.onFailure {
                    _uiState.value = FestivalListUiState.Error
                    analyticsHelper.logNetworkFailure(KEY_LOAD_FESTIVALS_LOG, it.message.toString())
                }
        }
    }

    fun showTicketReserve(festivalId: Long) {
        viewModelScope.launch {
            _event.emit(ShowTicketReserve(festivalId))
        }
    }

    companion object {
        private const val KEY_LOAD_FESTIVALS_LOG = "load_festivals"
    }
}
