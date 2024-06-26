package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.festago.festago.presentation.databinding.FragmentFestivalBookmarkBinding
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailArgs
import com.festago.festago.presentation.ui.festivaldetail.FestivalDetailArgs
import com.festago.festago.presentation.ui.home.bookmarklist.BookmarkListFragmentDirections
import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.adapater.FestivalBookmarkViewAdapter
import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.uistate.FestivalBookmarkUiState
import com.festago.festago.presentation.ui.signin.SignInActivity
import com.festago.festago.presentation.util.repeatOnStarted
import com.festago.festago.presentation.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FestivalBookmarkFragment : Fragment() {
    private var _binding: FragmentFestivalBookmarkBinding? = null
    private val binding get() = _binding!!

    private val vm: FestivalBookmarkViewModel by viewModels()

    private lateinit var festivalBookmarkViewAdapter: FestivalBookmarkViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFestivalBookmarkBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserve()
        vm.fetchBookmarkList()
    }

    private fun initView() {
        festivalBookmarkViewAdapter = FestivalBookmarkViewAdapter()
        binding.rvFestivalBookmarkList.adapter = festivalBookmarkViewAdapter

        binding.uiState = vm.uiState.value

        binding.refreshListener = { vm.fetchBookmarkList() }
        binding.loginListener = { vm.logIn() }
    }

    private fun initObserve() {
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiState.collect { uiState ->
                binding.uiState = uiState
                when (uiState) {
                    is FestivalBookmarkUiState.NotLoggedIn,
                    is FestivalBookmarkUiState.Loading,
                    is FestivalBookmarkUiState.Error,
                    -> Unit

                    is FestivalBookmarkUiState.Success ->
                        festivalBookmarkViewAdapter.submitList(uiState.festivalBookmarks)
                }
            }
        }

        repeatOnStarted(viewLifecycleOwner) {
            vm.event.collect { event ->
                when (event) {
                    is FestivalBookmarkEvent.ShowFestivalDetail -> {
                        findNavController().safeNavigate(
                            BookmarkListFragmentDirections.actionBookmarkListFragmentToFestivalDetailFragment(
                                with(event.festival) { FestivalDetailArgs(id, name, imageUrl) },
                            ),
                        )
                    }

                    is FestivalBookmarkEvent.ShowArtistDetail -> {
                        findNavController().safeNavigate(
                            BookmarkListFragmentDirections.actionBookmarkListFragmentToArtistDetailFragment(
                                with(event.artist) { ArtistDetailArgs(id, name, imageUrl) },
                            ),
                        )
                    }

                    is FestivalBookmarkEvent.ShowSignIn -> {
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
