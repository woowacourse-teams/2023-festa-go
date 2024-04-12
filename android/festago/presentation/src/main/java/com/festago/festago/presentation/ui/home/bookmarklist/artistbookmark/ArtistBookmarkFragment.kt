package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentArtistBookmarkBinding
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistBookmarkFragment : Fragment() {
    private var _binding: FragmentArtistBookmarkBinding? = null
    private val binding get() = _binding!!

    private val vm: ArtistBookmarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArtistBookmarkBinding.inflate(inflater)
        initView()
        initObserve()
        vm.fetchBookmarkList()
        return binding.root
    }

    private fun initView() {
        binding.uiState = vm.uiState.value

        binding.rvArtistBookmarkList.adapter = MyItemRecyclerViewAdapter(
            listOf(
                ArtistBookmarkViewHolder.of(binding.rvArtistBookmarkList).apply {
                    bind("1", "Artist 1")
                },
                ArtistBookmarkViewHolder.of(binding.rvArtistBookmarkList).apply {
                    bind("1", "Artist 1")
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
                    is ArtistBookmarkListUiState.Loading -> {
                        // Handle loading
                    }

                    is ArtistBookmarkListUiState.Success -> {
                        binding.rvArtistBookmarkList.adapter = MyItemRecyclerViewAdapter(
                            uiState.artistBookmarks.map { artistBookmark ->
                                ArtistBookmarkViewHolder.of(binding.rvArtistBookmarkList).apply {
                                    bind(
                                        artistBookmark.artist.name,
                                        artistBookmark.artist.profileImageUrl
                                    )
                                }
                            },
                        )
                    }

                    is ArtistBookmarkListUiState.Error -> {
                        // Handle error
                    }
                }
            }
        }
    }
}
