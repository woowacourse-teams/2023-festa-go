package com.festago.festago.presentation.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentSearchBinding
import com.festago.festago.presentation.ui.search.recentsearch.RecentSearchAdapter
import com.festago.festago.presentation.ui.search.uistate.SearchUiState
import com.festago.festago.presentation.util.repeatOnStarted
import com.festago.festago.presentation.util.setOnApplyWindowInsetsCompatListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val vm: SearchViewModel by viewModels()

    private lateinit var recentSearchAdapter: RecentSearchAdapter

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
    }

    private fun updateUi(uiState: SearchUiState) {
        when (uiState) {
            is SearchUiState.Loading,
            is SearchUiState.Error,
            -> Unit

            is SearchUiState.RecentSearchSuccess -> handleSuccessRecentSearchSuccess(uiState)
        }
    }

    private fun handleSuccessRecentSearchSuccess(uiState: SearchUiState.RecentSearchSuccess) {
        recentSearchAdapter.submitList(uiState.recentSearchQueries)
    }

    private fun initView() {
        initRecyclerView()
        initBack()
        initSearch()
        initDeleteAll()
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
                true
            } else {
                false
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
