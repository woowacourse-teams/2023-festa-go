package com.festago.festago.presentation.ui.studentsverification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.model.timer.Timer
import com.festago.festago.model.timer.TimerListener
import com.festago.festago.repository.SchoolRepository
import com.festago.festago.repository.StudentsVerificationRepository
import kotlinx.coroutines.launch

class StudentsVerificationViewModel(
    private val schoolRepository: SchoolRepository,
    private val studentsVerificationRepository: StudentsVerificationRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState =
        MutableLiveData<StudentVerificationUiState>(StudentVerificationUiState.Loading)
    val uiState: LiveData<StudentVerificationUiState> = _uiState

    private val timer: Timer = Timer()

    fun loadSchoolEmail(schoolId: Int) {
        viewModelScope.launch {
            schoolRepository.loadSchoolEmail(schoolId)
                .onSuccess { email ->
                    _uiState.value = StudentVerificationUiState.Success(
                        schoolEmail = email,
                        remainTime = MIN_REMAIN_TIME,
                    )
                }
                .onFailure {
                    _uiState.value = StudentVerificationUiState.Error
                    analyticsHelper.logNetworkFailure(
                        KEY_LOAD_SCHOOL_EMAIL_LOG,
                        it.message.toString(),
                    )
                }
        }
    }

    fun sendVerificationCode(userName: String, schoolId: Int) {
        viewModelScope.launch {
            studentsVerificationRepository.sendVerificationCode(userName, schoolId)
                .onSuccess {
                    val state = uiState.value
                    if (state is StudentVerificationUiState.Success) {
                        _uiState.value = state.copy(remainTime = TIMER_PERIOD)
                        setTimer()
                    }
                }
                .onFailure {
                    analyticsHelper.logNetworkFailure(
                        KEY_SEND_VERIFICATION_CODE_LOG,
                        it.message.toString(),
                    )
                }
        }
    }

    private suspend fun setTimer() {
        timer.timerListener = createTimerListener()
        timer.start(TIMER_PERIOD)
    }

    private fun createTimerListener(): TimerListener = object : TimerListener {
        override fun onTick(current: Int) {
            val state = uiState.value
            if (state is StudentVerificationUiState.Success) {
                _uiState.value = state.copy(remainTime = current)
            }
        }

        override fun onFinish() {
            val state = uiState.value
            if (state is StudentVerificationUiState.Success) {
                _uiState.value = state.copy(remainTime = MIN_REMAIN_TIME)
            }
        }
    }

    companion object {
        private const val KEY_LOAD_SCHOOL_EMAIL_LOG = "load_school_email"
        private const val KEY_SEND_VERIFICATION_CODE_LOG = "send_verification_code"
        private const val MIN_REMAIN_TIME = 0
        private const val TIMER_PERIOD = 300
    }
}
