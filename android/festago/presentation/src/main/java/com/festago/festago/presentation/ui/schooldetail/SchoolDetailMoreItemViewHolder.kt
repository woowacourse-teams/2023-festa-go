package com.festago.festago.presentation.ui.schooldetail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.festago.festago.presentation.databinding.ItemSchoolDetailMoreItemBinding
import com.festago.festago.presentation.ui.schooldetail.uistate.MoreItemUiState

class SchoolDetailMoreItemViewHolder(
    binding: ItemSchoolDetailMoreItemBinding
) : SchoolDetailViewHolder(binding) {
    fun bind(item: MoreItemUiState) {
        item.requestMore()
    }

    companion object {
        fun of(parent: ViewGroup): SchoolDetailMoreItemViewHolder {
            val binding = ItemSchoolDetailMoreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return SchoolDetailMoreItemViewHolder(binding)
        }
    }
}
