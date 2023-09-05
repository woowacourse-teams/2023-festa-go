package com.festago.festago.presentation.ui.studentsverification

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.databinding.ActivitySignInBinding
import com.festago.festago.presentation.ui.FestagoViewModelFactory

class StudentVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    private val vm: StudentsVerificationViewModel by viewModels { FestagoViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
