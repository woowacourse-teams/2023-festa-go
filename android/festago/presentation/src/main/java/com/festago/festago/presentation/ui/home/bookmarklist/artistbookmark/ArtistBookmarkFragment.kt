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

    private lateinit var artistBookmarkAdapter: ArtistBookmarkAdapter

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

        binding.refreshListener = { vm.fetchBookmarkList() }

        artistBookmarkAdapter = ArtistBookmarkAdapter()
        binding.rvArtistBookmarkList.adapter = artistBookmarkAdapter
    }

    private fun initObserve() {
        repeatOnStarted(this) {
            vm.uiState.collect { uiState ->
                binding.uiState = uiState
                when (uiState) {
                    is ArtistBookmarkListUiState.Loading -> {
                        // Handle loading
                    }

                    is ArtistBookmarkListUiState.Success -> {
                        artistBookmarkAdapter.submitList(uiState.artistBookmarks)
                    }

                    is ArtistBookmarkListUiState.Error -> {
                        // Handle error
                    }
                }
            }
        }
    }
}
