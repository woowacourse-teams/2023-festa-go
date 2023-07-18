package com.festago.festagostaff.presentation.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festagostaff.databinding.ActivityMainBinding
import com.festago.festagostaff.presentation.ui.ticketvalidation.TicketValidationActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initObserve()
    }

    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.vm = vm
    }

    private fun initObserve() {
        vm.event.observe(this) {
            handleEvent(it)
        }
    }

    private fun handleEvent(event: MainEvent) = when (event) {
        is MainEvent.OpenTicketValidation -> startActivity(TicketValidationActivity.getIntent(this))
    }
}
