package com.festago.festago.presentation.ui.selectschool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.repository.SchoolRepository
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
class SelectSchoolViewModel @Inject constructor(
    private val schoolRepository: SchoolRepository,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<SelectSchoolUiState>(SelectSchoolUiState.Loading)
    val uiState: StateFlow<SelectSchoolUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SelectSchoolEvent>()
    val event: SharedFlow<SelectSchoolEvent> = _event.asSharedFlow()

    fun loadSchools() {
        viewModelScope.launch {
            schoolRepository.loadSchools()
                .onSuccess { schools ->
                    _uiState.value = SelectSchoolUiState.Success(
                        schools.map {
                            SchoolUiState(
                                id = it.id,
                                domain = it.domain,
                                name = it.name,
                                selectedSchool = ::selectSchool,
                            )
                        })
                }
                .onFailure {
                    _uiState.value = SelectSchoolUiState.Error
                    analyticsHelper.logNetworkFailure(KEY_LOAD_SCHOOLS_LOG, it.message.toString())
                }
        }
    }

    fun selectSchool(schoolId: Long) {
        if (uiState.value is SelectSchoolUiState.Success) {
            _uiState.value =
                (uiState.value as SelectSchoolUiState.Success).copy(selectedSchoolId = schoolId)
        }
    }

    fun showStudentVerification() {
        viewModelScope.launch {
            if (uiState.value is SelectSchoolUiState.Success) {
                val success = uiState.value as SelectSchoolUiState.Success
                success.selectedSchoolId?.let { schoolId ->
                    _event.emit(
                        SelectSchoolEvent.ShowStudentVerification(schoolId)
                    )
                }
            }
        }
    }

    companion object {
        private const val KEY_LOAD_SCHOOLS_LOG = "load_schools"
    }
}
