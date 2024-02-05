package com.festago.festago.presentation.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailFragment
import com.festago.festago.presentation.ui.home.festivallist.FestivalListFragment

class HomeFragmentFactory(
    private val onArtistClick: (Long) -> Unit,
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (loadFragmentClass(classLoader, className)) {
            FestivalListFragment::class.java -> FestivalListFragment(onArtistClick)
            ArtistDetailFragment::class.java -> ArtistDetailFragment(onArtistClick)

            else -> super.instantiate(classLoader, className)
        }
    }
}