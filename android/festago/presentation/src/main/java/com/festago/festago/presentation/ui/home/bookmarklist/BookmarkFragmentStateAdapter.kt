package com.festago.festago.presentation.ui.home.bookmarklist

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark.ArtistBookmarkFragment

class BookmarkFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ArtistBookmarkFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
