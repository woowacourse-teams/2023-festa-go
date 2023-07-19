package com.festago.festagostaff.presentation.ui.ticketvalidation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festagostaff.data.RetrofitClient
import com.festago.festagostaff.data.repository.TicketDefaultRepository
import com.festago.festagostaff.databinding.ActivityTicketValidationBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.DefaultDecoderFactory

class TicketValidationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketValidationBinding

    private val vm: TicketValidationViewModel by viewModels {
        TicketValidationViewModel.TicketScanViewModelFactory(
            TicketDefaultRepository(
                RetrofitClient.getInstance().ticketRetrofitService,
            ),
        )
    }

    private val barcodeCallback = BarcodeCallback { result ->
        if (result.text == null) {
            Toast.makeText(this, "No content", Toast.LENGTH_SHORT).show()
            return@BarcodeCallback
        }
        if (!vm.isLatestCode(result.text)) {
            vm.validateTicketCode(result.text)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
    }

    private fun initBinding() {
        binding = ActivityTicketValidationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.vm = vm
        binding.dbvScanner.apply {
            decoderFactory = DefaultDecoderFactory(listOf(BarcodeFormat.QR_CODE))
            initializeFromIntent(intent)
            decodeContinuous(barcodeCallback)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.dbvScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.dbvScanner.pause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return binding.dbvScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, TicketValidationActivity::class.java)
        }
    }
}
