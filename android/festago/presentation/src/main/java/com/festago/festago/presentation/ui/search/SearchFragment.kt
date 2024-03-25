package com.festago.festago.presentation.ui.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.FragmentSearchBinding
import com.festago.festago.presentation.ui.search.recentsearch.RecentSearchAdapter
import com.festago.festago.presentation.ui.search.screen.ItemSearchScreenUiState
import com.festago.festago.presentation.ui.search.screen.ItemSearchScreenUiState.ArtistSearchScreen
import com.festago.festago.presentation.ui.search.screen.ItemSearchScreenUiState.FestivalSearchScreen
import com.festago.festago.presentation.ui.search.screen.ItemSearchScreenUiState.SchoolSearchScreen
import com.festago.festago.presentation.ui.search.screen.SearchScreenAdapter
import com.festago.festago.presentation.ui.search.uistate.SearchUiState
import com.festago.festago.presentation.util.repeatOnStarted
import com.festago.festago.presentation.util.setOnApplyWindowInsetsCompatListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

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
                ItemSearchScreenUiState.FESTIVAL_POSITION -> getString(R.string.search_tl_tab_festival)
                ItemSearchScreenUiState.ARTIST_POSITION -> getString(R.string.search_tl_tab_Artist)
                ItemSearchScreenUiState.SCHOOL_POSITION -> getString(R.string.search_tl_tab_school)
                else -> ""
            }
            tab.view.setOnClickListener {
                val currentScreen = when (tab.position) {
                    ItemSearchScreenUiState.FESTIVAL_POSITION -> FestivalSearchScreen(listOf())
                    ItemSearchScreenUiState.ARTIST_POSITION -> ArtistSearchScreen(listOf())
                    ItemSearchScreenUiState.SCHOOL_POSITION -> SchoolSearchScreen(listOf())
                    else -> FestivalSearchScreen(listOf())
                }
                vm.currentScreen = currentScreen
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
        binding.etSearch.setOnKeyListener { editText, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                vm.search(binding.etSearch.text.toString())
                val inputMethodManager =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
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
                FestivalSearchScreen(uiState.searchedFestivals, ::requestAddSearchQuery),
                ArtistSearchScreen(uiState.searchedArtists, ::requestAddSearchQuery),
                SchoolSearchScreen(uiState.searchedSchools, ::requestAddSearchQuery),
            ),
        )
        initSearchTab()
    }

    private fun initSearchTab() {
        binding.vpSearch.doOnNextLayout {
            binding.vpSearch.setCurrentItem(vm.currentScreen.screenPosition, false)
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

            is SearchEvent.UpdateSearchQuery -> {
                binding.etSearch.setText(event.searchQuery)
            }
        }
    }

    private fun requestAddSearchQuery() {
        startBrowser("https://forms.gle/y17dmCFw1jAYLR9H7")
    }

    private fun startBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
