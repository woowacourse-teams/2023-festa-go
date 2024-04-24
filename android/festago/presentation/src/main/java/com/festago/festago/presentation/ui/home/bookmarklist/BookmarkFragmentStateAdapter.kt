package com.festago.festago.presentation.ui.home.bookmarklist

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark.ArtistBookmarkFragment
import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.FestivalBookmarkFragment
import com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark.SchoolBookmarkFragment

class BookmarkFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FestivalBookmarkFragment()
            1 -> ArtistBookmarkFragment()
            2 -> SchoolBookmarkFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
