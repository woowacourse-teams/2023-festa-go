package com.festago.festago.presentation.ui.home.festivallist.bottomsheet

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.domain.model.festival.SchoolRegion
import com.festago.festago.presentation.ui.home.festivallist.uistate.SchoolRegionUiState

class RegionAdapter(
    private val items: List<SchoolRegionUiState>,
    private val onRegionSelect: (SchoolRegion) -> Unit,
    private val onDismiss: () -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RegionItemViewHolder.of(parent, onRegionSelect, onDismiss)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RegionItemViewHolder).bind(items[position])
    }
}
