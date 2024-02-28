package com.festago.festago.presentation.ui.festivaldetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentFestivalDetailBinding
import com.festago.festago.presentation.databinding.ItemMediaBinding
import com.festago.festago.presentation.ui.festivaldetail.adapter.stage.StageListAdapter
import com.festago.festago.presentation.ui.festivaldetail.uiState.FestivalDetailUiState
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FestivalDetailFragment : Fragment() {

    private var _binding: FragmentFestivalDetailBinding? = null
    private val binding get() = _binding!!

    private val vm: FestivalDetailViewModel by viewModels()

    private lateinit var adapter: StageListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFestivalDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
    }

    private fun initView() {
        val id = requireArguments().getLong(FESTIVAL_ID)
        adapter = StageListAdapter()
        binding.rvStageList.adapter = adapter
        vm.loadFestivalDetail(id)
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
                binding.uiState = it
                updateUi(it)
            }
        }
    }

    private fun updateUi(uiState: FestivalDetailUiState) {
        when (uiState) {
            is FestivalDetailUiState.Loading,
            is FestivalDetailUiState.Error,
            -> Unit

            is FestivalDetailUiState.Success -> handleSuccess(uiState)
        }
    }

    private fun handleSuccess(uiState: FestivalDetailUiState.Success) {
        binding.successUiState = uiState
        adapter.submitList(uiState.stages)
        binding.llcFestivalSocialMedia.removeAllViews()
        uiState.festival.socialMedias.forEach { media ->
            with(ItemMediaBinding.inflate(layoutInflater, binding.llcFestivalSocialMedia, false)) {
                imageUrl = media.logoUrl
                name = media.name
                clMedia.setOnClickListener { startBrowser(media.url) }
                binding.llcFestivalSocialMedia.addView(clMedia)
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
        private const val FESTIVAL_ID = "FESTIVAL_ID"

        fun newInstance(id: Long) = FestivalDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(FESTIVAL_ID, id)
            }
        }
    }
}
