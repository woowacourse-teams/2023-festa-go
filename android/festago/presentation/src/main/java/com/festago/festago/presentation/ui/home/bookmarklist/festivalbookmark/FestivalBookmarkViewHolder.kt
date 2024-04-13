package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.FragmentFestivalBookmarkItemBinding

class FestivalBookmarkViewHolder(val binding: FragmentFestivalBookmarkItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var id: String = ""
    var content: String = ""

    fun bind(id: String, content: String) {
        this.id = id
        this.content = content
    }

    companion object {
        fun of(parent: ViewGroup): FestivalBookmarkViewHolder {
            val binding = FragmentFestivalBookmarkItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalBookmarkViewHolder(binding)
        }
    }
}
