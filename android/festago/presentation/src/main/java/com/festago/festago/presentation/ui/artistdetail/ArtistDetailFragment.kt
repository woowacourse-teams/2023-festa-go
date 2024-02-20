package com.festago.festago.presentation.ui.artistdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.FragmentArtistDetailBinding
import com.festago.festago.presentation.databinding.ItemMediaBinding
import com.festago.festago.presentation.ui.artistdetail.adapter.festival.ArtistDetailAdapter
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistDetailUiState
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistDetailFragment : Fragment() {
    private var _binding: FragmentArtistDetailBinding? = null
    private val binding get() = _binding!!

    private val vm: ArtistDetailViewModel by viewModels()

    private val adapter = ArtistDetailAdapter { artistId ->
        // TODO: Navigation으로 변경
        requireActivity().supportFragmentManager.commit {
            add(R.id.fcvHomeContainer, newInstance(artistId))
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN)
            addToBackStack(null)
        }
    }

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
        val id = requireArguments().getLong(KEY_ID)
        initView(id)
        initObserve()
    }

    private fun initView(id: Long) {
        vm.loadArtistDetail(id)

        binding.rvToDoList.adapter = adapter

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.cvBookmark.setOnClickListener {
            binding.ivBookmark.isSelected = !binding.ivBookmark.isSelected
        }
    }

    private fun initObserve() {
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiState.collect {
                updateUi(it)
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
        binding.uiState = uiState
        adapter.submitList(uiState.stages)

        binding.llcArtistMedia.removeAllViews()

        uiState.artist.artistMedia.map { media ->
            with(ItemMediaBinding.inflate(layoutInflater, binding.llcArtistMedia, false)) {
                imageUrl = media.logoUrl
                ivImage.setOnClickListener { startBrowser(media.url) }
                binding.llcArtistMedia.addView(ivImage)
            }
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

    companion object {
        private const val KEY_ID = "ID"

        fun newInstance(
            id: Long,
        ) = ArtistDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_ID, id)
            }
        }
    }
}
