package com.festago.festago.presentation.ui.schooldetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.repository.SchoolRepository
import com.festago.festago.presentation.ui.schooldetail.uistate.ArtistUiState
import com.festago.festago.presentation.ui.schooldetail.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.schooldetail.uistate.SchoolDetailUiState
import com.festago.festago.presentation.ui.schooldetail.uistate.SchoolUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolDetailViewModel @Inject constructor(
    private val schoolRepository: SchoolRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SchoolDetailUiState>(SchoolDetailUiState.Loading)
    val uiState: StateFlow<SchoolDetailUiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<SchoolDetailEvent> = MutableSharedFlow()
    val event: SharedFlow<SchoolDetailEvent> = _event.asSharedFlow()

    fun loadSchoolDetail(schoolId: Long) {
        viewModelScope.launch {
            val deferredSchoolInfo = async { schoolRepository.loadSchoolInfo(schoolId) }
            val deferredFestivalPage = async { schoolRepository.loadSchoolFestivals(schoolId) }

            runCatching {
                val schoolInfo = deferredSchoolInfo.await().getOrThrow()
                val festivalPage = deferredFestivalPage.await().getOrThrow()

                _uiState.value = SchoolDetailUiState.Success(
                    schoolInfo = schoolInfo,
                    festivals = festivalPage.festivals.map { it.toUiState() },
                    isLast = festivalPage.isLastPage
                )
            }.onFailure {
                _uiState.value = SchoolDetailUiState.Error
                analyticsHelper.logNetworkFailure(
                    key = KEY_LOAD_SCHOOL_DETAIL,
                    value = it.message.toString(),
                )
            }
        }
    }

    private fun Festival.toUiState() = FestivalItemUiState(
        id = id,
        name = name,
        startDate = startDate,
        endDate = endDate,
        imageUrl = imageUrl,
        schoolUiState = SchoolUiState(
            id = school.id,
            name = school.name,
        ),
        artists = artists.map { artist ->
            ArtistUiState(
                id = artist.id,
                name = artist.name,
                imageUrl = artist.imageUrl,
                onArtistDetailClick = { id ->
                    viewModelScope.launch {
                        _event.emit(SchoolDetailEvent.ShowArtistDetail(id))
                    }
                }
            )
        },
        onFestivalDetailClick = { id ->
            viewModelScope.launch {
                _event.emit(SchoolDetailEvent.ShowFestivalDetail(id))
            }
        }
    )

    companion object {
        private const val KEY_LOAD_SCHOOL_DETAIL = "KEY_LOAD_SCHOOL_DETAIL"
    }
}
