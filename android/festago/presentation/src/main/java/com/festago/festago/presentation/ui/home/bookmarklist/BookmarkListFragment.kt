package com.festago.festago.presentation.ui.home.bookmarklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.festago.festago.presentation.databinding.FragmentBookmarkListBinding
import com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark.ArtistBookmarkFragment
import com.festago.festago.presentation.util.setOnApplyWindowInsetsCompatListener
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
        binding.root.setOnApplyWindowInsetsCompatListener { view, windowInsets ->
            val statusBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBarInsets.top, 0, 0)
            windowInsets
        }
        initView()
        return binding.root
    }

    private fun initView() {
        binding.vpBookmarkList.adapter = BookmarkFragmentStateAdapter(this)
    }
}
class BookmarkFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ArtistBookmarkFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
