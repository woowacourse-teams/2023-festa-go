package com.festago.festago.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class ReservationCompleteUiModel(
    val ticketId: Long,
    val number: Int,
    val entryTime: LocalDateTime,
) : Parcelable
