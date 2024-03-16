package com.festago.festago.presentation.ui.reservationcomplete

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class ReservedTicketArg(
    val ticketId: Long,
    val number: Int,
    val entryTime: LocalDateTime,
) : Parcelable
