package com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.FragmentSchoolBookmarkItemBinding

class SchoolBookmarkViewHolder(val binding: FragmentSchoolBookmarkItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var id: String = ""
    var content: String = ""

    fun bind(id: String, content: String) {
        this.id = id
        this.content = content
    }

    companion object {
        fun of(parent: ViewGroup): SchoolBookmarkViewHolder {
            val binding = FragmentSchoolBookmarkItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return SchoolBookmarkViewHolder(binding)
        }
    }
}
