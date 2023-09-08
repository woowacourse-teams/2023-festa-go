package com.festago.festago.presentation.ui.studentsverification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.model.StudentVerificationCode
import com.festago.festago.model.timer.Timer
import com.festago.festago.model.timer.TimerListener
import com.festago.festago.repository.SchoolRepository
import com.festago.festago.repository.StudentsVerificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentsVerificationViewModel(
    private val schoolRepository: SchoolRepository,
    private val studentsVerificationRepository: StudentsVerificationRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<StudentVerificationUiState>(StudentVerificationUiState.Loading)
    val uiState: StateFlow<StudentVerificationUiState> = _uiState.asStateFlow()

    val studentVerificationCode = MutableStateFlow("")

    private val timer: Timer = Timer()

    init {
        validateStudentVerificationCode()
    }

    private fun validateStudentVerificationCode() {
        viewModelScope.launch {
            studentVerificationCode.collect { code ->
                checkValidateCodeWhenSuccess(code)
            }
        }
    }

    private fun checkValidateCodeWhenSuccess(code: String) {
        val state = uiState.value
        if (state is StudentVerificationUiState.Success) {
            updateIsValidation(code, state)
        }
    }

    private fun updateIsValidation(
        code: String,
        state: StudentVerificationUiState.Success,
    ) {
        runCatching {
            StudentVerificationCode(code)
        }.onSuccess {
            _uiState.value = state.copy(isValidateCode = true)
        }.onFailure {
            _uiState.value = state.copy(isValidateCode = false)
        }
    }

    fun loadSchoolEmail(schoolId: Long) {
        if (uiState.value is StudentVerificationUiState.Success) return
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

    fun sendVerificationCode(userName: String, schoolId: Long) {
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

    fun confirmVerificationCode() {
        viewModelScope.launch {
            studentsVerificationRepository.requestVerificationCodeConfirm(
                StudentVerificationCode(studentVerificationCode.value),
            )
        }
    }

    companion object {
        private const val KEY_LOAD_SCHOOL_EMAIL_LOG = "load_school_email"
        private const val KEY_SEND_VERIFICATION_CODE_LOG = "send_verification_code"
        private const val MIN_REMAIN_TIME = 0
        private const val TIMER_PERIOD = 300
    }
}
