package com.festago.festago.presentation.ui.home.bookmarklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentBookmarkListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkListFragment : Fragment() {
    private var _binding: FragmentBookmarkListBinding? = null
    private val binding get() = _binding!!

    private val vm: BookmarkListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBookmarkListBinding.inflate(inflater)
        return binding.root
    }
}
