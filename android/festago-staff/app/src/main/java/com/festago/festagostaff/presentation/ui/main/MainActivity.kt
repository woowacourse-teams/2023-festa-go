package com.festago.festagostaff.presentation.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.festago.festagostaff.databinding.ActivityMainBinding
import com.festago.festagostaff.presentation.ui.ticketscan.TicketScanActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheckTicket.setOnClickListener {
            startActivity(TicketScanActivity.getIntent(this))
        }
    }
}
