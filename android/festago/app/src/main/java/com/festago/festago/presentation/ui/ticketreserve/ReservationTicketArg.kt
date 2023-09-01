package com.festago.festago.presentation.ui.ticketreserve

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReservationTicketArg(
    val id: Int,
    val remainAmount: Int,
    val ticketType: String,
    val totalAmount: Int,
) : Parcelable
