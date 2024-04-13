package com.festago.festago.presentation.ui.home.bookmarklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.festago.festago.presentation.databinding.FragmentBookmarkListBinding
import com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark.ArtistBookmarkFragment
import com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark.ArtistBookmarkViewModel
import com.festago.festago.presentation.util.setOnApplyWindowInsetsCompatListener
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkListFragment : Fragment() {
    private var _binding: FragmentBookmarkListBinding? = null
    private val binding get() = _binding!!

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

        binding.vpBookmarkList.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.tlBookmarkListTab.getTabAt(0)?.select()

                    1 -> binding.tlBookmarkListTab.getTabAt(1)?.select()
                    2 -> binding.tlBookmarkListTab.getTabAt(2)?.select()
                }
            }
        })

        binding.tlBookmarkListTab.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> binding.vpBookmarkList.currentItem = 0
                    1 -> binding.vpBookmarkList.currentItem = 1
                    2 -> binding.vpBookmarkList.currentItem = 2
                }
            }
        })
    }
}
