package com.festago.festago.presentation.ui.ticketreserve.bottomsheet

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BottomSheetReservationTicketArg(
    val id: Int,
    val remainAmount: Int,
    val ticketType: BottomSheetTicketTypeArg,
    val totalAmount: Int,
) : Parcelable
