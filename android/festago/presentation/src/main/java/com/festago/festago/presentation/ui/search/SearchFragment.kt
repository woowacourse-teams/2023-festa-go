package com.festago.festago.presentation.ui.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.FragmentSearchBinding
import com.festago.festago.presentation.ui.search.recentsearch.RecentSearchAdapter
import com.festago.festago.presentation.ui.search.screen.ItemSearchScreenUiState.ArtistSearchScreen
import com.festago.festago.presentation.ui.search.screen.ItemSearchScreenUiState.FestivalSearchScreen
import com.festago.festago.presentation.ui.search.screen.ItemSearchScreenUiState.SchoolSearchScreen
import com.festago.festago.presentation.ui.search.screen.SearchScreenAdapter
import com.festago.festago.presentation.ui.search.uistate.SearchUiState
import com.festago.festago.presentation.util.repeatOnStarted
import com.festago.festago.presentation.util.setOnApplyWindowInsetsCompatListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val vm: SearchViewModel by viewModels()

    private lateinit var recentSearchAdapter: RecentSearchAdapter
    private lateinit var searchScreenAdapter: SearchScreenAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater)
        binding.root.setOnApplyWindowInsetsCompatListener { view, windowInsets ->
            val statusBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBarInsets.top, 0, 0)
            windowInsets
        }
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
        repeatOnStarted(viewLifecycleOwner) {
            vm.event.collect {
                handleEvent(it)
            }
        }
    }

    private fun updateUi(uiState: SearchUiState) {
        when (uiState) {
            is SearchUiState.Loading,
            is SearchUiState.Error,
            -> Unit

            is SearchUiState.RecentSearchSuccess -> handleRecentSearchSuccess(uiState)
            is SearchUiState.SearchSuccess -> handleSuccessSearch(uiState)
        }
    }

    private fun initView() {
        initRecyclerView()
        initBack()
        initSearch()
        initDeleteAll()
        initViewPager()
    }

    private fun initViewPager() {
        searchScreenAdapter = SearchScreenAdapter()
        binding.vpSearch.adapter = searchScreenAdapter
        TabLayoutMediator(binding.tlSearch, binding.vpSearch) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.search_tl_tab_festival)
                1 -> getString(R.string.search_tl_tab_Artist)
                2 -> getString(R.string.search_tl_tab_school)
                else -> ""
            }
        }.attach()
    }

    private fun initRecyclerView() {
        recentSearchAdapter = RecentSearchAdapter()
        binding.rvRecentSearch.adapter = recentSearchAdapter
    }

    private fun initBack() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initDeleteAll() {
        binding.tvDeleteAll.setOnClickListener {
            vm.deleteAllRecentSearch()
        }
    }

    private fun initSearch() {
        binding.etSearch.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                vm.search(binding.etSearch.text.toString())
                binding.etSearch.text?.clear()
                true
            } else {
                false
            }
        }
        vm.loadRecentSearch()
    }

    private fun handleRecentSearchSuccess(uiState: SearchUiState.RecentSearchSuccess) {
        recentSearchAdapter.submitList(uiState.recentSearchQueries)
    }

    private fun handleSuccessSearch(uiState: SearchUiState.SearchSuccess) {
        searchScreenAdapter.submitList(
            listOf(
                FestivalSearchScreen(uiState.searchedFestivals),
                ArtistSearchScreen(uiState.searchedArtists),
                SchoolSearchScreen(uiState.searchedSchools),
            ),
        )
        initSearchTab()
    }

    private fun initSearchTab() {
        with(binding.tlSearch) {
            addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        binding.tlSearch.clearAnimation()
                        val currentScreen = when (tab.position) {
                            0 -> FestivalSearchScreen(listOf())
                            1 -> ArtistSearchScreen(listOf())
                            2 -> SchoolSearchScreen(listOf())
                            else -> FestivalSearchScreen(listOf())
                        }
                        vm.currentScreen = currentScreen
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) = Unit
                    override fun onTabReselected(tab: TabLayout.Tab) = Unit
                },
            )
        }

        binding.vpSearch.doOnPreDraw {
            binding.vpSearch.setCurrentItem(vm.currentScreen.screenPosition, false)
            Log.d("asdf", vm.currentScreen.screenPosition.toString())
        }
    }

    private fun handleEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.ShowFestivalDetail -> {
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToFestivalDetailFragment(
                        event.festivalId,
                    ),
                )
            }

            is SearchEvent.ShowArtistDetail -> {
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToArtistDetailFragment(
                        event.artistId,
                    ),
                )
            }

            is SearchEvent.ShowSchoolDetail -> {
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToSchoolDetailFragment(
                        event.schoolId,
                    ),
                )
            }

            is SearchEvent.SearchBlank -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.search_cant_search_blank),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
