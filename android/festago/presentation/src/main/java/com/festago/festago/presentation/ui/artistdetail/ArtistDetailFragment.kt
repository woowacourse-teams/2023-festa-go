package com.festago.festago.presentation.ui.artistdetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.FragmentArtistDetailBinding
import com.festago.festago.presentation.ui.artistdetail.adapter.festival.ArtistDetailAdapter
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistDetailUiState
import com.festago.festago.presentation.util.repeatOnStarted
import com.google.android.material.chip.Chip
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
        uiState.artist.artistMedia.forEach { media ->
            binding.cgArtistMedia.addView(
                (layoutInflater.inflate(R.layout.chip_media, binding.cgArtistMedia, false) as Chip)
                    .apply {
                        syncIcon(media.logoUrl)
                        text = media.name
                        setOnClickListener {
                            // TODO: 유지보수하기 쉽게 변경
                            val intent =
                                requireContext().packageManager.getLaunchIntentForPackage("com.android.chrome")
                            if (intent != null) {
                                intent.data = android.net.Uri.parse(media.url)
                                startActivity(intent)
                            }
                        }
                    },
            )
        }
    }

    private fun Chip.syncIcon(url: String) {
        Glide.with(this)
            .load(url)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable>,
                    isFirstResource: Boolean,
                ): Boolean {
                    chipIcon =
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_launcher_background,
                            null,
                        )
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any?,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean,
                ): Boolean {
                    chipIcon = resource
                    return false
                }
            }).preload()
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
