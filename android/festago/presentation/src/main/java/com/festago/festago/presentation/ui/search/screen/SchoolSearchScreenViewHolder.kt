package com.festago.festago.presentation.ui.search.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.festago.festago.presentation.databinding.ItemSchoolSearchScreenBinding
import com.festago.festago.presentation.ui.search.schoolSearchAdatper.SchoolSearchAdapter

class SchoolSearchScreenViewHolder(private val binding: ItemSchoolSearchScreenBinding) :
    SearchScreenViewHolder(binding) {
    private val schoolSearchAdapter: SchoolSearchAdapter = SchoolSearchAdapter()

    init {
        binding.rvSchools.adapter = schoolSearchAdapter
    }

    fun bind(item: ItemSearchScreenUiState.SchoolSearchScreen) {
        schoolSearchAdapter.submitList(item.schoolSearches)
        setNoResultVisibility(item)
        binding.btnFestago.setOnClickListener {
            item.onAddSearchQueryClick()
        }
    }

    private fun setNoResultVisibility(item: ItemSearchScreenUiState.SchoolSearchScreen) {
        val visibility = if (item.schoolSearches.isEmpty()) View.VISIBLE else View.GONE
        binding.tvNoResult.visibility = visibility
        binding.tvNoResultGuide.visibility = visibility
        binding.btnFestago.visibility = visibility
    }

    companion object {
        fun of(parent: ViewGroup): SchoolSearchScreenViewHolder {
            val binding = ItemSchoolSearchScreenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return SchoolSearchScreenViewHolder(binding)
        }
    }
}
