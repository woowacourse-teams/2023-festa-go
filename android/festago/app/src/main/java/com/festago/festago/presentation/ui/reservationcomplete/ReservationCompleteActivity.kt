package com.festago.festago.presentation.ui.reservationcomplete

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.databinding.ActivityReservationCompleteBinding
import com.festago.festago.presentation.model.ReservationCompleteUiModel
import com.festago.festago.presentation.util.getParcelableExtraCompat

class ReservationCompleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val reservationComplete =
            intent.getParcelableExtraCompat<ReservationCompleteUiModel>(
                KEY_RESERVATION_COMPLETE,
            )
        binding.reservationComplete = reservationComplete
    }

    companion object {
        private const val KEY_RESERVATION_COMPLETE = "KEY_RESERVATION_COMPLETE"

        fun getIntent(context: Context, reservationComplete: ReservationCompleteUiModel): Intent {
            return Intent(context, ReservationCompleteActivity::class.java).apply {
                putExtra(KEY_RESERVATION_COMPLETE, reservationComplete)
            }
        }
    }
}
