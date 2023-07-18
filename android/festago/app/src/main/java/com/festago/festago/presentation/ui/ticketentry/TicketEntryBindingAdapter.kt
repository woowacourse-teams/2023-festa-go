package com.festago.festago.presentation.ui.ticketentry

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.festago.festago.R
import com.festago.festago.presentation.model.TicketStateUiModel
import com.festago.festago.presentation.model.TicketStateUiModel.AFTER_ENTRY
import com.festago.festago.presentation.model.TicketStateUiModel.AWAY
import com.festago.festago.presentation.model.TicketStateUiModel.BEFORE_ENTRY
import com.festago.festago.presentation.model.TicketStateUiModel.EMPTY
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

@BindingAdapter("ticketBackground")
fun setTicketBackgroundByState(viewGroup: ViewGroup, state: TicketStateUiModel) {
    val background = when (state) {
        BEFORE_ENTRY, AWAY, EMPTY -> R.drawable.bg_ticket_gradient_primary
        AFTER_ENTRY -> R.drawable.bg_ticket_gradient_secondary
    }
    viewGroup.setBackgroundResource(background)
}

@BindingAdapter("renewBackground")
fun setRenewBackgroundByState(imageView: ImageView, state: TicketStateUiModel) {
    val background = when (state) {
        BEFORE_ENTRY, AWAY, EMPTY -> R.drawable.btn_circle_primary
        AFTER_ENTRY -> R.drawable.btn_circle_secondary
    }
    imageView.setBackgroundResource(background)
}

@BindingAdapter("remainTimeProgressDrawable")
fun ProgressBar.setRemainTimeProgressDrawableByState(state: TicketStateUiModel) {
    progressDrawable = when (state) {
        BEFORE_ENTRY, AWAY, EMPTY -> ResourcesCompat.getDrawable(
            resources,
            R.drawable.pb_ticket_remain_time_primary,
            null,
        )

        AFTER_ENTRY -> ResourcesCompat.getDrawable(
            resources,
            R.drawable.pb_ticket_remain_time_secondary,
            null,
        )
    }
}
