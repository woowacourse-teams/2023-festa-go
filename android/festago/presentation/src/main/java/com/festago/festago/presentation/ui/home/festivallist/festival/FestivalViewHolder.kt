package com.festago.festago.presentation.ui.home.festivallist.festival

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.festago.festago.presentation.databinding.ItemFestivalListFestivalBinding
import com.festago.festago.presentation.ui.home.festivallist.festival.artistlist.ArtistAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState

class FestivalViewHolder(
    private val binding: ItemFestivalListFestivalBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val artistAdapter = ArtistAdapter()

    init {
        binding.rvFestivalArtists.adapter = artistAdapter
        binding.rvFestivalArtists.addItemDecoration(ArtistItemDecoration(binding.root.context))
    }

    fun bind(item: FestivalItemUiState) {
        binding.item = item
        artistAdapter.submitList(item.artists)
    }

    private class ArtistItemDecoration(val context: Context) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.right = 8.dpToPx(context)
        }

        private fun Int.dpToPx(context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                context.resources.displayMetrics,
            ).toInt()
        }
    }

    companion object {
        fun of(parent: ViewGroup): FestivalViewHolder {
            val binding = ItemFestivalListFestivalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalViewHolder(binding)
        }
    }
}
