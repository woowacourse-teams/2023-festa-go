package com.festago.festago.presentation.ui.studentsverification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.R
import com.festago.festago.databinding.ActivityStudentVerificationBinding
import com.festago.festago.presentation.ui.FestagoViewModelFactory
import com.festago.festago.presentation.util.repeatOnStarted
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class StudentVerificationActivity : AppCompatActivity() {

    private val binding: ActivityStudentVerificationBinding by lazy {
        ActivityStudentVerificationBinding.inflate(layoutInflater)
    }

    private val vm: StudentsVerificationViewModel by viewModels { FestagoViewModelFactory }

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
            vm.sendVerificationCode(binding.tieVerificationCode.text.toString(), schoolId)
        }
    }

    private fun initObserve() {
        repeatOnStarted {
            vm.uiState.collect { uiState ->
                updateUiState(uiState)
            }
        }
    }

    private fun updateUiState(uiState: StudentVerificationUiState) {
        binding.uiState = uiState
        when (uiState) {
            is StudentVerificationUiState.Success -> handleSuccess(uiState)
            is StudentVerificationUiState.Loading, StudentVerificationUiState.Error -> Unit
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

    companion object {

        private const val KEY_SCHOOL_ID = "KEY_SCHOOL_ID"

        fun getIntent(context: Context, schoolId: Long): Intent {
            return Intent(context, StudentVerificationActivity::class.java).apply {
                putExtra(KEY_SCHOOL_ID, schoolId)
            }
        }
    }
}
