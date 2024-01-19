package com.festago.festago.presentation.ui.home.festivallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.festago.festago.presentation.databinding.FragmentFestivalListBinding
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.PopularFestivalViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FestivalListFragment : Fragment() {

    private var _binding: FragmentFestivalListBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularFestivalViewPager: PopularFestivalViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFestivalListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        popularFestivalViewPager = PopularFestivalViewPagerAdapter(
            context = requireContext(),
            foregroundViewPager = binding.vpPopularFestivalForeground,
            backgroundViewPager = binding.vpPopularFestivalBackground,
        )

        TabLayoutMediator(
            binding.intoTabLayout,
            binding.vpPopularFestivalForeground,
        ) { tab, position -> }.attach()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
