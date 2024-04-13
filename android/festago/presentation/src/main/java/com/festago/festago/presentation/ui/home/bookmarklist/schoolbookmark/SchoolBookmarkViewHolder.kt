package com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemSchoolBookmarkBinding

class SchoolBookmarkViewHolder(val binding: ItemSchoolBookmarkBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SchoolBookmarkUiState) {
        binding.school = item
    }

    companion object {
        fun of(parent: ViewGroup): SchoolBookmarkViewHolder {
            val binding = ItemSchoolBookmarkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return SchoolBookmarkViewHolder(binding)
        }
    }
}
