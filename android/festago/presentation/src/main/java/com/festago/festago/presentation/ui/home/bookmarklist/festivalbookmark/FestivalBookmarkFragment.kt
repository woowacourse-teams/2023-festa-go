package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentFestivalBookmarkBinding
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FestivalBookmarkFragment : Fragment() {
    private var _binding: FragmentFestivalBookmarkBinding? = null
    private val binding get() = _binding!!

    private val vm: FestivalBookmarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFestivalBookmarkBinding.inflate(inflater)
        initView()
        initObserve()
        vm.fetchBookmarkList()
        return binding.root
    }

    private fun initView() {
        binding.uiState = vm.uiState.value

        binding.refreshListener =  { vm.fetchBookmarkList() }

        binding.rvFestivalBookmarkList.adapter = FestivalBookmarkViewAdapter(
            listOf(
                FestivalBookmarkViewHolder.of(binding.rvFestivalBookmarkList).apply {
                    bind("1", "Festival 1")
                },
                FestivalBookmarkViewHolder.of(binding.rvFestivalBookmarkList).apply {
                    bind("1", "Festival 1")
                },
            ),
        )
    }

    private fun initObserve() {
        repeatOnStarted(this) {
            vm.uiState.collect { uiState ->
                println("uiState: $uiState")
                binding.uiState = uiState
                when (uiState) {
                    is FestivalBookmarkListUiState.Loading -> {
                        // Handle loading
                    }

                    is FestivalBookmarkListUiState.Success -> {
                        binding.rvFestivalBookmarkList.adapter = FestivalBookmarkViewAdapter(
                            uiState.FestivalBookmarks.map { FestivalBookmark ->
                                FestivalBookmarkViewHolder.of(binding.rvFestivalBookmarkList).apply {
                                    bind(
                                        FestivalBookmark.festival.name,
                                        FestivalBookmark.festival.imageUrl,
                                    )
                                }
                            },
                        )
                    }

                    is FestivalBookmarkListUiState.Error -> {
                        // Handle error
                    }
                }
            }
        }
    }
}
