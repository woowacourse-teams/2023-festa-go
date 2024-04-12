package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.FragmentArtistBookmarkItemBinding

class ArtistBookmarkViewHolder(val binding: FragmentArtistBookmarkItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var id: String = ""
    var content: String = ""

    fun bind(id: String, content: String) {
        this.id = id
        this.content = content
    }

    companion object {
        fun of(parent: ViewGroup): ArtistBookmarkViewHolder {
            val binding = FragmentArtistBookmarkItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistBookmarkViewHolder(binding)
        }
    }
}
