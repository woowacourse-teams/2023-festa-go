package com.festago.festago.presentation.ui.ticketentry

import android.view.ViewGroup
import android.widget.Button
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
fun ImageView.setQrCodeImage(content: String) {
    val bitmap = BarcodeEncoder().encodeBitmap(
        content,
        BarcodeFormat.QR_CODE,
        200,
        200,
    )
    setImageBitmap(bitmap)
}

@BindingAdapter("ticketBackground")
fun ViewGroup.setTicketBackgroundByState(state: TicketStateUiModel) {
    val background = when (state) {
        BEFORE_ENTRY, EMPTY -> R.drawable.bg_ticket_gradient_primary
        AFTER_ENTRY -> R.drawable.bg_ticket_gradient_secondary
        AWAY -> R.drawable.bg_ticket_gradient_tertiary
    }
    setBackgroundResource(background)
}

@BindingAdapter("renewBackground")
fun ImageView.setRenewBackgroundByState(state: TicketStateUiModel) {
    val background = when (state) {
        BEFORE_ENTRY, EMPTY -> R.drawable.btn_circle_primary
        AFTER_ENTRY -> R.drawable.btn_circle_secondary
        AWAY -> R.drawable.btn_circle_tertiary
    }
    setBackgroundResource(background)
}

@BindingAdapter("remainTimeProgressDrawable")
fun ProgressBar.setRemainTimeProgressDrawableByState(state: TicketStateUiModel) {
    progressDrawable = when (state) {
        BEFORE_ENTRY, EMPTY -> ResourcesCompat.getDrawable(
            resources,
            R.drawable.pb_ticket_remain_time_primary,
            null,
        )

        AFTER_ENTRY -> ResourcesCompat.getDrawable(
            resources,
            R.drawable.pb_ticket_remain_time_secondary,
            null,
        )

        AWAY -> ResourcesCompat.getDrawable(
            resources,
            R.drawable.pb_ticket_remain_time_tertiary,
            null,
        )
    }
}

@BindingAdapter("ticketStateBackground")
fun Button.setTicketStateBackgroundByState(state: TicketStateUiModel) {
    val colorRes = when (state) {
        BEFORE_ENTRY, EMPTY -> R.color.md_theme_light_primary
        AFTER_ENTRY -> R.color.md_theme_light_secondary
        AWAY -> R.color.md_theme_light_tertiary
    }
    backgroundTintList = ResourcesCompat.getColorStateList(resources, colorRes, null)
}
