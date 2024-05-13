package com.festago.festago.presentation.ui.festivaldetail.adapter.stage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemFestivalDetailStageBinding
import com.festago.festago.presentation.ui.festivaldetail.adapter.artist.ArtistAdapter
import com.festago.festago.presentation.ui.festivaldetail.uiState.StageItemUiState

class StageViewHolder(
    private val binding: ItemFestivalDetailStageBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val artistAdapter = ArtistAdapter()

    init {
        binding.rvStageArtists.adapter = artistAdapter
        binding.rvStageArtists.itemAnimator = null
    }

    fun bind(item: StageItemUiState) {
        binding.item = item
        artistAdapter.submitList(item.artists)
    }

    companion object {
        fun of(parent: ViewGroup): StageViewHolder {
            val binding = ItemFestivalDetailStageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return StageViewHolder(binding)
        }
    }
}
