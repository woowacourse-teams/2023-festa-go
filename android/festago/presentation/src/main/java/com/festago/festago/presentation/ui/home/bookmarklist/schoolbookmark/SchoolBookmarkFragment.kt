package com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.festago.festago.presentation.databinding.FragmentSchoolBookmarkBinding
import com.festago.festago.presentation.ui.home.bookmarklist.BookmarkListFragmentDirections
import com.festago.festago.presentation.ui.schooldetail.SchoolDetailArgs
import com.festago.festago.presentation.ui.signin.SignInActivity
import com.festago.festago.presentation.util.repeatOnStarted
import com.festago.festago.presentation.util.safeNavigate
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserve()
        vm.fetchBookmarkList()
    }

    private fun initView() {
        binding.uiState = vm.uiState.value

        binding.refreshListener = { vm.fetchBookmarkList() }
        binding.loginListener = { vm.logIn() }
        schoolBookmarkAdapter = SchoolBookmarkAdapter()
        binding.rvSchoolBookmarkList.adapter = schoolBookmarkAdapter
    }

    private fun initObserve() {
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiState.collect { uiState ->
                binding.uiState = uiState
                when (uiState) {
                    is SchoolBookmarkListUiState.Loading,
                    is SchoolBookmarkListUiState.Error,
                    is SchoolBookmarkListUiState.NotLoggedIn,
                    -> Unit

                    is SchoolBookmarkListUiState.Success -> {
                        schoolBookmarkAdapter.submitList(uiState.schoolBookmarks)
                    }
                }
            }
        }
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiEvent.collect { event ->
                when (event) {
                    is SchoolBookmarkEvent.ShowSchoolDetail -> {
                        findNavController().safeNavigate(
                            BookmarkListFragmentDirections.actionBookmarkListFragmentToSchoolDetailFragment(
                                with(event.school) { SchoolDetailArgs(id, name, imageUrl) },
                            ),
                        )
                    }

                    is SchoolBookmarkEvent.ShowSignIn -> {
                        startActivity(SignInActivity.getIntent(requireContext()))
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
