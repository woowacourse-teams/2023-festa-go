package com.festago.festago.presentation.ui.reservationcomplete

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.databinding.ActivityReservationCompleteBinding
import com.festago.festago.presentation.util.getParcelableExtraCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReservationCompleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
    }

    private fun initBinding() {
        binding = ActivityReservationCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initView() {
        val reservedTicket =
            intent.getParcelableExtraCompat<ReservedTicketArg>(
                KEY_RESERVATION_COMPLETE,
            )
        binding.reservedTicket = reservedTicket
    }

    companion object {
        private const val KEY_RESERVATION_COMPLETE = "KEY_RESERVATION_COMPLETE"

        fun getIntent(context: Context, reservationComplete: ReservedTicketArg): Intent {
            return Intent(context, ReservationCompleteActivity::class.java).apply {
                putExtra(KEY_RESERVATION_COMPLETE, reservationComplete)
            }
        }
    }
}
