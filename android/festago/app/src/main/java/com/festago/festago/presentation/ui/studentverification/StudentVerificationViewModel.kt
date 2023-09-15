package com.festago.festago.presentation.ui.studentverification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.logNetworkFailure
import com.festago.festago.model.StudentVerificationCode
import com.festago.festago.model.timer.Timer
import com.festago.festago.model.timer.TimerListener
import com.festago.festago.repository.SchoolRepository
import com.festago.festago.repository.StudentVerificationRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentVerificationViewModel(
    private val schoolRepository: SchoolRepository,
    private val studentVerificationRepository: StudentVerificationRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<StudentVerificationUiState>(StudentVerificationUiState.Loading)
    val uiState: StateFlow<StudentVerificationUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<StudentVerificationEvent>()
    val event: SharedFlow<StudentVerificationEvent> = _event.asSharedFlow()

    val verificationCode = MutableStateFlow("")

    private val timer: Timer = Timer()

    init {
        initObserveVerificationCode()
    }

    private fun initObserveVerificationCode() {
        viewModelScope.launch {
            verificationCode.collect { code ->
                handleOnUiStateSuccess {
                    validateCode(code, it)
                }
            }
        }
    }

    private inline fun handleOnUiStateSuccess(action: (state: StudentVerificationUiState.Success) -> Unit) {
        val state = uiState.value as? StudentVerificationUiState.Success ?: return
        action(state)
    }

    private fun validateCode(verificationCode: String, state: StudentVerificationUiState.Success) {
        runCatching {
            StudentVerificationCode(verificationCode)
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
            studentVerificationRepository.sendVerificationCode(userName, schoolId)
                .onSuccess {
                    handleOnUiStateSuccess { state ->
                        _uiState.value = state.copy(remainTime = TIMER_PERIOD)
                    }
                    setTimer()
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
            handleOnUiStateSuccess { state ->
                _uiState.value = state.copy(remainTime = current)
            }
        }

        override fun onFinish() {
            handleOnUiStateSuccess { state ->
                _uiState.value = state.copy(remainTime = MIN_REMAIN_TIME)
            }
        }
    }

    fun confirmVerificationCode() {
        viewModelScope.launch {
            val state = uiState.value as? StudentVerificationUiState.Success ?: return@launch

            if (state.remainTime == MIN_REMAIN_TIME) {
                _event.emit(StudentVerificationEvent.CodeTimeOut)
                return@launch
            }

            studentVerificationRepository.requestVerificationCodeConfirm(
                StudentVerificationCode(verificationCode.value),
            ).onSuccess {
                _event.emit(StudentVerificationEvent.VerificationSuccess)
            }.onFailure {
                _event.emit(StudentVerificationEvent.VerificationFailure)
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
