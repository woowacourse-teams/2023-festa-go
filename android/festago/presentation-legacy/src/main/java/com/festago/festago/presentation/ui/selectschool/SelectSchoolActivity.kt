package com.festago.festago.presentation.ui.selectschool

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ActivitySelectSchoolBinding
import com.festago.festago.presentation.ui.studentverification.StudentVerificationActivity
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectSchoolActivity : AppCompatActivity() {

    private val binding: ActivitySelectSchoolBinding by lazy {
        ActivitySelectSchoolBinding.inflate(layoutInflater)
    }

    private val vm: SelectSchoolViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initObserve()
        initView()
    }

    private fun initBinding() {
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.vm = vm
    }

    private fun initObserve() {
        repeatOnStarted(this) {
            vm.uiState.collect { uiState ->
                handleUiState(uiState)
            }
        }
        repeatOnStarted(this) {
            vm.event.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun initView() {
        vm.loadSchools()
    }

    private fun handleUiState(uiState: SelectSchoolUiState) {
        binding.uiState = uiState
        when (uiState) {
            is SelectSchoolUiState.Loading, is SelectSchoolUiState.Error -> Unit
            is SelectSchoolUiState.Success -> handleSuccess(uiState)
        }
    }

    private fun handleSuccess(uiState: SelectSchoolUiState.Success) {
        val adapter =
            ArrayAdapter(this, R.layout.item_select_school, uiState.schools.map { it.name })
        binding.actvSelectSchool.setAdapter(adapter)
        binding.actvSelectSchool.setOnItemClickListener { _, _, position, _ ->
            val selectedSchool = uiState.schools.firstOrNull {
                it.name == adapter.getItem(position)
            }
            selectedSchool?.let { vm.selectSchool(it.id) }
        }
    }

    private fun handleEvent(event: SelectSchoolEvent) {
        when (event) {
            is SelectSchoolEvent.ShowStudentVerification -> {
                startActivity(StudentVerificationActivity.getIntent(this, event.schoolId))
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SelectSchoolActivity::class.java)
        }
    }
}
