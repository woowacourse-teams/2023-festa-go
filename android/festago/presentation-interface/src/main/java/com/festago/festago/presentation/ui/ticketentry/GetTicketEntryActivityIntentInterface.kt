package com.festago.festago.presentation.ui.ticketentry

import android.content.Context
import android.content.Intent

fun interface GetTicketEntryActivityIntentInterface {
    operator fun invoke(context: Context, ticketId: Long): Intent
}
