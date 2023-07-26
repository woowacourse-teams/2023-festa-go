package com.festago.festago.presentation.ui.ticketreserve.bottomSheet

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.model.ReservationTicketUiModel

class TicketReserveBottomSheetAdapter :
    ListAdapter<ReservationTicketUiModel, TicketReserveBottomViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TicketReserveBottomViewHolder {
        return TicketReserveBottomViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TicketReserveBottomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ReservationTicketUiModel>() {
            override fun areContentsTheSame(
                oldItem: ReservationTicketUiModel,
                newItem: ReservationTicketUiModel,
            ) = oldItem == newItem

            override fun areItemsTheSame(
                oldItem: ReservationTicketUiModel,
                newItem: ReservationTicketUiModel,
            ) = oldItem.ticketType == newItem.ticketType
        }
    }
}
