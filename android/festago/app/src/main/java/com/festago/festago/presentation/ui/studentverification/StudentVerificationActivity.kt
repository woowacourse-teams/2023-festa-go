package com.festago.festago.presentation.ui.studentverification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.R
import com.festago.festago.databinding.ActivityStudentVerificationBinding
import com.festago.festago.presentation.ui.customview.OkDialogFragment
import com.festago.festago.presentation.ui.home.HomeActivity
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class StudentVerificationActivity : AppCompatActivity() {

    private val binding: ActivityStudentVerificationBinding by lazy {
        ActivityStudentVerificationBinding.inflate(layoutInflater)
    }

    private val vm: StudentVerificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        initObserve()
    }

    private fun initBinding() {
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.vm = vm
    }

    private fun initView() {
        val schoolId = intent.getLongExtra(KEY_SCHOOL_ID, -1L)
        vm.loadSchoolEmail(schoolId)
        initRequestVerificationCodeBtn(schoolId)
    }

    private fun initRequestVerificationCodeBtn(schoolId: Long) {
        binding.btnRequestVerificationCode.setOnClickListener {
            vm.sendVerificationCode(binding.tieUserName.text.toString(), schoolId)
        }
    }

    private fun initObserve() {
        repeatOnStarted {
            vm.uiState.collect { uiState ->
                handleUiState(uiState)
            }
        }
        repeatOnStarted {
            vm.event.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleUiState(uiState: StudentVerificationUiState) {
        binding.uiState = uiState
        when (uiState) {
            is StudentVerificationUiState.Loading -> Unit
            is StudentVerificationUiState.Success -> handleSuccess(uiState)
            is StudentVerificationUiState.Error -> Unit
        }
    }

    private fun handleSuccess(uiState: StudentVerificationUiState.Success) {
        binding.tvSchoolEmail.text =
            getString(R.string.student_verification_tv_email_format, uiState.schoolEmail)

        val format =
            DateTimeFormatter.ofPattern(getString(R.string.student_verification_tv_timer_format))
        binding.tvTimerVerificationCode.text = LocalTime.ofSecondOfDay(uiState.remainTime.toLong())
            .format(format)

        binding.btnVerificationConfirm.isEnabled = uiState.isValidateCode
    }

    private fun handleEvent(event: StudentVerificationEvent) {
        when (event) {
            is StudentVerificationEvent.VerificationSuccess -> handleVerificationSuccess()
            is StudentVerificationEvent.VerificationFailure -> showDialog(FAILURE_VERIFICATION)
            is StudentVerificationEvent.VerificationTimeOut -> showDialog(TIME_OUT_VERIFICATION)
            is StudentVerificationEvent.SendingEmailFailure -> showDialog(FAILURE_SENDING_EMAIL)
        }
    }

    private fun handleVerificationSuccess() {
        val intent = HomeActivity.getIntent(this).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        finishAffinity()
        startActivity(intent)
    }

    private fun showDialog(message: String) {
        val dialog = OkDialogFragment.newInstance(message)
        dialog.show(supportFragmentManager, OkDialogFragment::class.java.name)
    }

    companion object {
        private const val KEY_SCHOOL_ID = "KEY_SCHOOL_ID"
        private const val FAILURE_VERIFICATION = "인증에 실패하였습니다"
        private const val TIME_OUT_VERIFICATION = "인증 시간이 만료되었습니다"
        private const val FAILURE_SENDING_EMAIL = "이메일 입력을 확인해주세요"

        fun getIntent(context: Context, schoolId: Long): Intent {
            return Intent(context, StudentVerificationActivity::class.java).apply {
                putExtra(KEY_SCHOOL_ID, schoolId)
            }
        }
    }
}
