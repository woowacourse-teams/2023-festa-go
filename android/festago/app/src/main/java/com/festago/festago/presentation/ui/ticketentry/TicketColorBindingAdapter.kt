package com.festago.festago.presentation.ui.ticketentry

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import com.festago.festago.R
import com.festago.festago.presentation.model.TicketStateUiModel

@BindingAdapter("ticketBackground")
fun setTicketBackgroundByState(viewGroup: ViewGroup, state: TicketStateUiModel) {
    val background = when (state) {
        TicketStateUiModel.BEFORE_ENTRY -> R.drawable.bg_ticket_gradient_primary
        TicketStateUiModel.AFTER_ENTRY -> R.drawable.bg_ticket_gradient_secondary
        TicketStateUiModel.AWAY -> R.drawable.bg_ticket_gradient_primary
        TicketStateUiModel.EMPTY -> R.drawable.bg_ticket_gradient_primary
    }
    viewGroup.setBackgroundResource(background)
}
