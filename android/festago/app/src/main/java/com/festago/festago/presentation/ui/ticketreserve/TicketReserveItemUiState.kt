package com.festago.festago.presentation.ui.ticketreserve

import android.os.Parcelable
import com.festago.festago.presentation.model.ReservationTicketUiModel
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class TicketReserveItemUiState(
    val id: Int,
    val lineUp: String,
    val startTime: LocalDateTime,
    val ticketOpenTime: LocalDateTime,
    val reservationTickets: List<ReservationTicketUiModel>,
    val canReserve: Boolean,
    val isSigned: Boolean,
    val onShowStageTickets: (stageId: Int, stageStartTime: LocalDateTime) -> Unit,
) : Parcelable
