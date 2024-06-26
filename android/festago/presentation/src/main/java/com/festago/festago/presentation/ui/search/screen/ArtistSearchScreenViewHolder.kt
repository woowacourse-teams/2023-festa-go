package com.festago.festago.presentation.ui.search.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.festago.festago.presentation.databinding.ItemArtistSearchScreenBinding
import com.festago.festago.presentation.ui.search.artistsearch.ArtistSearchAdapter

class ArtistSearchScreenViewHolder(
    private val binding: ItemArtistSearchScreenBinding,
) : SearchScreenViewHolder(binding) {

    private val artistSearchAdapter: ArtistSearchAdapter = ArtistSearchAdapter()

    init {
        binding.rvArtists.adapter = artistSearchAdapter
    }

    fun bind(item: ItemSearchScreenUiState.ArtistSearchScreen) {
        artistSearchAdapter.submitList(item.artistSearches)
        setNoResultVisibility(item)
        binding.btnFestago.setOnClickListener {
            item.onAddSearchQueryClick()
        }
    }

    private fun setNoResultVisibility(item: ItemSearchScreenUiState.ArtistSearchScreen) {
        val visibility = if (item.artistSearches.isEmpty()) View.VISIBLE else View.GONE
        binding.tvNoResult.visibility = visibility
        binding.tvNoResultGuide.visibility = visibility
        binding.btnFestago.visibility = visibility
    }

    companion object {
        fun of(parent: ViewGroup): ArtistSearchScreenViewHolder {
            val binding = ItemArtistSearchScreenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistSearchScreenViewHolder(binding)
        }
    }
}
