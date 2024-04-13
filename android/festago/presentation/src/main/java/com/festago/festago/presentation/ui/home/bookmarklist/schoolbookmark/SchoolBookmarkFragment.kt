package com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentSchoolBookmarkBinding
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchoolBookmarkFragment : Fragment() {
    private var _binding: FragmentSchoolBookmarkBinding? = null
    private val binding get() = _binding!!

    private val vm: SchoolBookmarkViewModel by viewModels()

    private lateinit var schoolBookmarkAdapter: SchoolBookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSchoolBookmarkBinding.inflate(inflater)
        initView()
        initObserve()
        vm.fetchBookmarkList()
        return binding.root
    }

    private fun initView() {
        binding.uiState = vm.uiState.value

        binding.refreshListener = { vm.fetchBookmarkList() }

        schoolBookmarkAdapter = SchoolBookmarkAdapter()
        binding.rvSchoolBookmarkList.adapter = schoolBookmarkAdapter
    }

    private fun initObserve() {
        repeatOnStarted(this) {
            vm.uiState.collect { uiState ->
                binding.uiState = uiState
                when (uiState) {
                    is SchoolBookmarkListUiState.Loading -> {
                        // Handle loading
                    }

                    is SchoolBookmarkListUiState.Success -> {
                        schoolBookmarkAdapter.submitList(uiState.schoolBookmarks)
                    }

                    is SchoolBookmarkListUiState.Error -> {
                        // Handle error
                    }
                }
            }
        }
    }
}
