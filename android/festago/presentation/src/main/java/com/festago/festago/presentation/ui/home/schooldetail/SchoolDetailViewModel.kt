package com.festago.festago.presentation.ui.home.schooldetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.domain.repository.SchoolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun loadSchoolDetail() {
        viewModelScope.launch {
            val deferredSchoolInfo = async { schoolRepository.loadSchoolInfo() }
            val deferredFestivalPage = async { schoolRepository.loadSchoolFestivals() }

            runCatching {
                val schoolInfo = deferredSchoolInfo.await().getOrThrow()
                val festivalPage = deferredFestivalPage.await().getOrThrow()

                _uiState.value = SchoolDetailUiState.Success(
                    schoolInfo = schoolInfo,
                    festivals = festivalPage.festivals,
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

    companion object {
        private const val KEY_LOAD_SCHOOL_DETAIL = "KEY_LOAD_SCHOOL_DETAIL"
    }
}
