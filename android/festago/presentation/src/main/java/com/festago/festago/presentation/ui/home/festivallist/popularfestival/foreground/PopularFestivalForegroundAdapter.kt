package com.festago.festago.presentation.ui.home.festivallist.popularfestival.foreground

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState

class PopularFestivalForegroundAdapter(festivals: List<FestivalItemUiState> = listOf()) :
    RecyclerView.Adapter<PopularFestivalForegroundViewHolder>() {

    private val _festivals = festivals.toMutableList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PopularFestivalForegroundViewHolder {
        return PopularFestivalForegroundViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: PopularFestivalForegroundViewHolder, position: Int) {
        holder.bind(_festivals[position % _festivals.size])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    fun submitList(festivals: List<FestivalItemUiState>) {
        if (_festivals.toList() == festivals) {
            return
        }
        _festivals.clear()
        _festivals.addAll(festivals)
        notifyDataSetChanged()
    }
}
