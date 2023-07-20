package com.festago.festagostaff.presentation.ui.ticketvalidation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.festago.festagostaff.R
import com.festago.festagostaff.data.RetrofitClient
import com.festago.festagostaff.data.repository.TicketDefaultRepository
import com.festago.festagostaff.databinding.ActivityTicketValidationBinding
import com.festago.festagostaff.presentation.extension.showToast
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
            showToast(getString(R.string.ticket_validation_toast_no_content))
            return@BarcodeCallback
        }
        if (!vm.isLatestCode(result.text)) {
            vm.validateTicketCode(result.text)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
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

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.getOrNull(0) == PERMISSION_GRANTED) {
                showToast(getString(R.string.ticket_validation_toast_camera_permission_granted))
            } else {
                showToast(getString(R.string.ticket_validation_toast_camera_permission_denied))
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 111

        fun getIntent(context: Context): Intent {
            return Intent(context, TicketValidationActivity::class.java)
        }
    }
}
