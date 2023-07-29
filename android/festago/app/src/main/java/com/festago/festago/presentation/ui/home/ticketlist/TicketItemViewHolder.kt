package com.festago.festago.presentation.ui.home.ticketlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemTicketBinding
import com.festago.festago.presentation.model.TicketUiModel

class TicketItemViewHolder(
    val binding: ItemTicketBinding,
    vm: TicketListViewModel,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.vm = vm
    }

    fun bind(item: TicketUiModel) {
        binding.ticket = item
    }

    companion object {
        fun of(parent: ViewGroup, vm: TicketListViewModel): TicketItemViewHolder {
            val binding = ItemTicketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return TicketItemViewHolder(binding, vm)
        }
    }
}
