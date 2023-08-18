package com.festago.festago.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReservationTicketUiModel(
    val id: Int,
    val remainAmount: Int,
    val ticketType: String,
    val totalAmount: Int,
) : Parcelable
