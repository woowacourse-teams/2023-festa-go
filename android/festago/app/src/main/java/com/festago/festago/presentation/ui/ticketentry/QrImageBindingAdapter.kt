package com.festago.festago.presentation.ui.ticketentry

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

@BindingAdapter("qrContent")
fun setQrCodeImage(imageView: ImageView, content: String) {
    val barcodeEncoder = BarcodeEncoder()
    val bitmap = barcodeEncoder.encodeBitmap(
        content,
        BarcodeFormat.QR_CODE,
        200,
        200,
    )
    imageView.setImageBitmap(bitmap)
}
