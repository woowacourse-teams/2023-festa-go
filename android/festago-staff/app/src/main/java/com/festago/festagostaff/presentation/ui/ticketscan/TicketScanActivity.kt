package com.festago.festagostaff.presentation.ui.ticketscan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.festago.festagostaff.databinding.ActivityTicketScanBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class TicketScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketScanBinding

    private val vm: TicketScanViewModel by lazy {
        ViewModelProvider(this)[TicketScanViewModel::class.java]
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
        binding = ActivityTicketScanBinding.inflate(layoutInflater)
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
            return Intent(context, TicketScanActivity::class.java)
        }
    }
}
