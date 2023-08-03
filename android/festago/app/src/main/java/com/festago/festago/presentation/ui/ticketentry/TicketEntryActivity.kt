package com.festago.festago.presentation.ui.ticketentry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.data.RetrofitClient
import com.festago.festago.data.repository.TicketDefaultRepository
import com.festago.festago.databinding.ActivityTicketEntryBinding
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.ui.ticketentry.TicketEntryViewModel.TicketEntryViewModelFactory
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class TicketEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketEntryBinding

    private val vm: TicketEntryViewModel by viewModels {
        TicketEntryViewModelFactory(
            TicketDefaultRepository(
                ticketRetrofitService = RetrofitClient.getInstance().ticketRetrofitService,
            ),
            FirebaseAnalyticsHelper.getInstance(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentTicketId = intent.getLongExtra(KEY_TICKET_ID, 0L)

        initBinding()
        initObserve()
        initView(currentTicketId)
        initBackPressed()
    }

    private fun initBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(RESULT_OK, intent)
                finish()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initBinding() {
        binding = ActivityTicketEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.vm = vm
    }

    private fun initObserve() {
        vm.uiState.observe(this) { uiState ->
            binding.uiState = uiState
            when (uiState) {
                is TicketEntryUiState.Loading, is TicketEntryUiState.Error -> Unit
                is TicketEntryUiState.Success -> {
                    handleSuccess(uiState)
                }
            }
        }
    }

    private fun initView(currentTicketId: Long) {
        vm.loadTicket(currentTicketId)
    }

    private fun handleSuccess(uiState: TicketEntryUiState.Success) {
        binding.successState = uiState
        val ticketCode = uiState.ticketCode.toPresentation()

        val bitmap = BarcodeEncoder().encodeBitmap(
            ticketCode.code,
            BarcodeFormat.QR_CODE,
            200,
            200,
        )
        binding.ivQrCode.setImageBitmap(bitmap)
        binding.pbRemainTime.progressDrawable = ResourcesCompat.getDrawable(
            resources,
            uiState.progressBarBackgroundId,
            null,
        )
        binding.btnTicketCondition.backgroundTintList =
            ResourcesCompat.getColorStateList(
                resources,
                uiState.ticketConditionColor,
                null,
            )
        binding.btnTicketCondition.text = getString(uiState.ticketConditionTextId)
    }

    companion object {
        private const val KEY_TICKET_ID = "KEY_TICKET_ID"
        const val RESULT_OK = 1

        fun getIntent(context: Context, ticketId: Long): Intent {
            return Intent(context, TicketEntryActivity::class.java).apply {
                putExtra(KEY_TICKET_ID, ticketId)
            }
        }
    }
}
