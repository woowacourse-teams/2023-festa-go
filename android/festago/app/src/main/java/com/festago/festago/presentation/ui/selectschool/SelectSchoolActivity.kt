package com.festago.festago.presentation.ui.selectschool

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.R
import com.festago.festago.databinding.ActivitySelectSchoolBinding
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
    }

    private fun initBinding() {
        setContentView(binding.root)
    }

    private fun initObserve() {
        repeatOnStarted {
            vm.uiState.collect { uiState ->
                handleUiState(uiState)
            }
        }
    }

    private fun handleUiState(uiState: SelectSchoolUiState) {
        when (uiState) {
            is SelectSchoolUiState.Success -> {
                val adapter =
                    ArrayAdapter(this, R.layout.item_select_school, uiState.schools.map { it.name })
                binding.actvSelectSchool.setAdapter(adapter)
            }
        }
    }
}
