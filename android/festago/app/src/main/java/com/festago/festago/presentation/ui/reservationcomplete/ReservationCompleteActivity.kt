package com.festago.festago.presentation.ui.reservationcomplete

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.databinding.ActivityReservationCompleteBinding
import com.festago.festago.presentation.model.ReservedTicketUiModel
import com.festago.festago.presentation.util.getParcelableExtraCompat

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
        val reservationComplete =
            intent.getParcelableExtraCompat<ReservedTicketUiModel>(
                KEY_RESERVATION_COMPLETE,
            )
        binding.reservationComplete = reservationComplete
    }

    companion object {
        private const val KEY_RESERVATION_COMPLETE = "KEY_RESERVATION_COMPLETE"

        fun getIntent(context: Context, reservationComplete: ReservedTicketUiModel): Intent {
            return Intent(context, ReservationCompleteActivity::class.java).apply {
                putExtra(KEY_RESERVATION_COMPLETE, reservationComplete)
            }
        }
    }
}
