package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.festago.festago.presentation.databinding.FragmentArtistBookmarkBinding
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailArgs
import com.festago.festago.presentation.ui.home.bookmarklist.BookmarkListFragmentDirections
import com.festago.festago.presentation.ui.signin.SignInActivity
import com.festago.festago.presentation.util.repeatOnStarted
import com.festago.festago.presentation.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistBookmarkFragment : Fragment() {
    private var _binding: FragmentArtistBookmarkBinding? = null
    private val binding get() = _binding!!

    private val vm: ArtistBookmarkViewModel by viewModels()

    private lateinit var artistBookmarkAdapter: ArtistBookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArtistBookmarkBinding.inflate(inflater)
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

        artistBookmarkAdapter = ArtistBookmarkAdapter()
        binding.rvArtistBookmarkList.adapter = artistBookmarkAdapter
    }

    private fun initObserve() {
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiState.collect { uiState ->
                binding.uiState = uiState
                when (uiState) {
                    is ArtistBookmarkListUiState.NotLoggedIn,
                    is ArtistBookmarkListUiState.Loading,
                    is ArtistBookmarkListUiState.Error,
                    -> Unit

                    is ArtistBookmarkListUiState.Success -> {
                        artistBookmarkAdapter.submitList(uiState.artistBookmarks)
                    }
                }
            }
        }
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiEvent.collect { event ->
                when (event) {
                    is ArtistBookmarkEvent.ShowArtistDetail -> {
                        findNavController().safeNavigate(
                            BookmarkListFragmentDirections.actionBookmarkListFragmentToArtistDetailFragment(
                                with(event.artist) {
                                    ArtistDetailArgs(id, name, imageUrl)
                                },
                            ),
                        )
                    }

                    is ArtistBookmarkEvent.ShowSignIn -> {
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
