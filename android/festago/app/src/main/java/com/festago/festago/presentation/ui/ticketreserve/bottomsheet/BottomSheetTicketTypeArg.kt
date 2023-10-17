package com.festago.festago.presentation.ui.ticketreserve.bottomsheet

import android.os.Parcelable
import com.festago.festago.model.TicketType
import kotlinx.parcelize.Parcelize

@Parcelize
enum class BottomSheetTicketTypeArg : Parcelable {
    STUDENT, VISITOR, OTHER
    ;

    companion object {
        fun from(ticketType: TicketType): BottomSheetTicketTypeArg = when (ticketType) {
            TicketType.STUDENT -> STUDENT
            TicketType.VISITOR -> VISITOR
            TicketType.OTHER -> OTHER
        }
    }
}
