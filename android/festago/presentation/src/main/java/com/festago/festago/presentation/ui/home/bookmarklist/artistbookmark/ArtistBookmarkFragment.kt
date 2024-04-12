package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentArtistBookmarkBinding
import com.festago.festago.presentation.ui.home.bookmarklist.BookmarkListViewModel

class ArtistBookmarkFragment : Fragment() {
    private var _binding: FragmentArtistBookmarkBinding? = null
    private val binding get() = _binding!!

    private val vm: BookmarkListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArtistBookmarkBinding.inflate(inflater)

        initView()
        return binding.root
    }

    private fun initView() {
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
}
