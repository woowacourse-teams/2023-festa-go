package com.festago.festago.presentation.ui.studentsverification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.festago.festago.R
import com.festago.festago.databinding.ActivityStudentVerificationBinding
import com.festago.festago.presentation.ui.FestagoViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class StudentVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentVerificationBinding

    private val vm: StudentsVerificationViewModel by viewModels { FestagoViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initObserving()
        initView()
    }

    private fun initBinding() {
        binding = ActivityStudentVerificationBinding.inflate(layoutInflater)
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

    private fun initObserving() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    binding.uiState = uiState
                    when (uiState) {
                        is StudentVerificationUiState.Success -> handleSuccess(uiState)
                        is StudentVerificationUiState.Loading, StudentVerificationUiState.Error -> {}
                    }
                }
            }
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
