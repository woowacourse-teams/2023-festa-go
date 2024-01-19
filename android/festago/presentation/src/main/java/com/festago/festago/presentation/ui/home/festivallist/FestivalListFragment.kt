package com.festago.festago.presentation.ui.home.festivallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentFestivalListBinding
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.PopularFestivalViewPagerAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalListUiState
import com.festago.festago.presentation.util.repeatOnStarted
import com.google.android.material.tabs.TabLayoutMediator

class FestivalListFragment : Fragment() {

    private var _binding: FragmentFestivalListBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularFestivalViewPager: PopularFestivalViewPagerAdapter

    private val vm: FestivalListViewModel by viewModels()

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

        initObserve()
        initView()
    }

    private fun initObserve() {
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiState.collect {
                binding.uiState = it
                updateUi(it)
            }
        }
    }

    private fun initView() {
        initViewPager()
        vm.loadPopularFestival()
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

    private fun updateUi(uiState: FestivalListUiState) {
        when (uiState) {
            is FestivalListUiState.Loading,
            is FestivalListUiState.Error,
            -> Unit

            is FestivalListUiState.Success -> handleSuccess(uiState)
        }
    }

    private fun handleSuccess(uiState: FestivalListUiState.Success) {
        popularFestivalViewPager.submitList(uiState.festivals)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
