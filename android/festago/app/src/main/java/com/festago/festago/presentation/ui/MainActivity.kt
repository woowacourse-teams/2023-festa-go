package com.festago.festago.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.databinding.ActivityMainBinding
import com.festago.festago.presentation.ui.ticketentry.TicketEntryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initView()
    }

    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initView() {
        binding.btnEnterTicket.setOnClickListener {
            navigateToTicketEntryActivity()
        }
    }

    private fun navigateToTicketEntryActivity() {
        startActivity(TicketEntryActivity.getIntent(this, 0L))
    }
}
