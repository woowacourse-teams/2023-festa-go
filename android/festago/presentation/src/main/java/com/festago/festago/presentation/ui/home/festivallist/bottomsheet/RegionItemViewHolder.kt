package com.festago.festago.presentation.ui.home.festivallist.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.domain.model.festival.SchoolRegion
import com.festago.festago.presentation.databinding.ItemRegionBinding
import com.festago.festago.presentation.ui.home.festivallist.uistate.SchoolRegionUiState

class RegionItemViewHolder(
    private val binding: ItemRegionBinding,
    private val onRegionSelect: (SchoolRegion) -> Unit,
    private val onDismiss: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SchoolRegionUiState) {
        binding.item = item
        with(binding.tvRegion) {
            this.isSelected = item.isSelected
            setOnClickListener {
                onRegionSelect(item.schoolRegion)
                onDismiss.invoke()
            }
        }
    }

    companion object {
        fun of(
            parent: ViewGroup,
            onRegionSelect: (SchoolRegion) -> Unit,
            onDismiss: () -> Unit
        ): RegionItemViewHolder {
            val binding = ItemRegionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return RegionItemViewHolder(binding, onRegionSelect, onDismiss)
        }
    }
}
