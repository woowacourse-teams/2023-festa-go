package com.festago.festago.presentation.ui.artistdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.FragmentArtistDetailBinding
import com.festago.festago.presentation.databinding.ItemMediaBinding
import com.festago.festago.presentation.ui.artistdetail.adapter.festival.ArtistDetailAdapter
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistDetailUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.MoreItemUiState
import com.festago.festago.presentation.ui.bindingadapter.setImage
import com.festago.festago.presentation.ui.festivaldetail.FestivalDetailArgs
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
        initView()
        initObserve()
    }

    private fun initView() {
        loadArtistDetail()
        binding.rvToDoList.adapter = adapter
        initButton()
    }

    private fun loadArtistDetail() {
        binding.tvArtistName.text = args.artist.name
        binding.ivProfileImage.setImage(args.artist.profileUrl)
        val delayTimeMillis = resources.getInteger(R.integer.nav_Anim_time).toLong()
        vm.loadArtistDetail(args.artist.id, delayTimeMillis)
    }

    private fun initButton() {
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
        is ArtistDetailUiState.Loading -> Unit
        is ArtistDetailUiState.Success -> handleSuccess(uiState)
        is ArtistDetailUiState.Error -> handleError(uiState)
    }

    private fun handleSuccess(uiState: ArtistDetailUiState.Success) {
        binding.successUiState = uiState
        binding.tvArtistName.text = uiState.artist.artistName
        binding.ivProfileImage.setImage(uiState.artist.profileUrl)

        binding.ivBookmark.isSelected = uiState.bookMarked

        val items: List<Any> = if (uiState.isLast) {
            uiState.festivals
        } else {
            uiState.festivals + MoreItemUiState { vm.loadMoreArtistFestivals(args.artist.id) }
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

    private fun handleError(uiState: ArtistDetailUiState.Error) {
        binding.refreshListener = { uiState.refresh(args.artist.id) }
    }

    private fun handleEvent(event: ArtistDetailEvent) = when (event) {
        is ArtistDetailEvent.ShowArtistDetail -> {
            findNavController().navigate(
                ArtistDetailFragmentDirections.actionArtistDetailFragmentSelf(
                    with(event.artist) { ArtistDetailArgs(id, name, imageUrl) },
                ),
            )
        }

        is ArtistDetailEvent.ShowFestivalDetail -> {
            findNavController().navigate(
                ArtistDetailFragmentDirections.actionArtistDetailFragmentToFestivalDetailFragment(
                    with(event.festival) { FestivalDetailArgs(id, name, imageUrl) },
                ),
            )
        }

        is ArtistDetailEvent.FailedToFetchBookmarkList -> {
            Toast.makeText(requireContext(), "최대 북마크 갯수를 초과했습니다", Toast.LENGTH_SHORT)
                .show()
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
