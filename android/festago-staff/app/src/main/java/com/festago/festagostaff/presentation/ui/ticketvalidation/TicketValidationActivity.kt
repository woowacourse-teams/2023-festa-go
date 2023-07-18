package com.festago.festagostaff.presentation.ui.ticketvalidation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festagostaff.data.RetrofitClient
import com.festago.festagostaff.data.repository.TicketDefaultRepository
import com.festago.festagostaff.databinding.ActivityTicketValidationBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class TicketValidationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketValidationBinding

    private val vm: TicketValidationViewModel by viewModels {
        TicketValidationViewModel.TicketScanViewModelFactory(
            TicketDefaultRepository(
                RetrofitClient.getInstance().ticketRetrofitService,
            ),
        )
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "No content", Toast.LENGTH_SHORT).show()
            return@registerForActivityResult
        }
        vm.validateTicketCode(result.contents.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initBarcodeLauncher()
    }

    private fun initBinding() {
        binding = ActivityTicketValidationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.vm = vm
    }

    private fun initBarcodeLauncher() {
        val scanOptions = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        }
        barcodeLauncher.launch(scanOptions)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, TicketValidationActivity::class.java)
        }
    }
}
