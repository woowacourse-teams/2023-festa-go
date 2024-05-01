package com.festago.festago.presentation.ui.artistdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.festago.festago.presentation.databinding.FragmentArtistDetailBinding
import com.festago.festago.presentation.databinding.ItemMediaBinding
import com.festago.festago.presentation.ui.artistdetail.adapter.festival.ArtistDetailAdapter
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistDetailUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.MoreItemUiState
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistDetailFragment : Fragment() {
    private var _binding: FragmentArtistDetailBinding? = null
    private val binding get() = _binding!!

    private val vm: ArtistDetailViewModel by viewModels()

    private val args: ArtistDetailFragmentArgs by navArgs()

    private val adapter = ArtistDetailAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArtistDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(args.artistId)
        initObserve()
    }

    private fun initView(id: Long) {
        vm.loadArtistDetail(id)

        binding.rvToDoList.adapter = adapter

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.cvBookmark.isSelected
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

    private fun updateUi(uiState: ArtistDetailUiState) = when (uiState) {
        is ArtistDetailUiState.Loading,
        is ArtistDetailUiState.Error,
        -> Unit

        is ArtistDetailUiState.Success -> handleSuccess(uiState)
    }

    private fun handleSuccess(uiState: ArtistDetailUiState.Success) {
        binding.successUiState = uiState

        binding.ivBookmark.isSelected = uiState.bookMarked

        val items: List<Any> = if (uiState.isLast) {
            uiState.festivals
        } else {
            uiState.festivals + MoreItemUiState { vm.loadMoreArtistFestivals(args.artistId) }
        }
        adapter.submitList(items)

        binding.llcArtistMedia.removeAllViews()

        uiState.artist.artistMedia.map { media ->
            with(ItemMediaBinding.inflate(layoutInflater, binding.llcArtistMedia, false)) {
                imageUrl = media.logoUrl
                ivImage.setOnClickListener { startBrowser(media.url) }
                binding.llcArtistMedia.addView(ivImage)
            }
        }
    }

    private fun handleEvent(event: ArtistDetailEvent) = when (event) {
        is ArtistDetailEvent.ShowArtistDetail -> {
            findNavController().navigate(
                ArtistDetailFragmentDirections.actionArtistDetailFragmentSelf(event.artistId),
            )
        }

        is ArtistDetailEvent.ShowFestivalDetail -> {
            findNavController().navigate(
                ArtistDetailFragmentDirections.actionArtistDetailFragmentToFestivalDetailFragment(
                    event.festivalId,
                ),
            )
        }
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
