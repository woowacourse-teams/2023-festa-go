package com.festago.festago.presentation.ui.schooldetail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.FragmentSchoolDetailBinding
import com.festago.festago.presentation.databinding.ItemMediaBinding
import com.festago.festago.presentation.ui.schooldetail.uistate.MoreItemUiState
import com.festago.festago.presentation.ui.schooldetail.uistate.SchoolDetailUiState
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchoolDetailFragment : Fragment() {

    private var _binding: FragmentSchoolDetailBinding? = null
    private val binding get() = _binding!!

    private val vm: SchoolDetailViewModel by viewModels()

    private lateinit var adapter: SchoolFestivalListAdapter

    private val args: SchoolDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSchoolDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(args.schoolId)
        initObserve()
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

    private fun initView(schoolId: Long) {
        adapter = SchoolFestivalListAdapter()
        binding.rvFestivalList.adapter = adapter
        val delayTimeMillis = resources.getInteger(R.integer.nav_Anim_time).toLong()
        vm.loadSchoolDetail(schoolId, delayTimeMillis)
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.cvBookmark.setOnClickListener {
            binding.ivBookmark.isSelected = !binding.ivBookmark.isSelected
        }
    }

    private fun updateUi(uiState: SchoolDetailUiState) {
        when (uiState) {
            is SchoolDetailUiState.Loading,
            is SchoolDetailUiState.Error,
            -> Unit

            is SchoolDetailUiState.Success -> handleSuccess(uiState)
        }
    }

    private fun handleSuccess(uiState: SchoolDetailUiState.Success) {
        binding.successUiState = uiState

        val items: List<Any> = if (uiState.isLast) {
            uiState.festivals
        } else {
            uiState.festivals + MoreItemUiState { vm.loadMoreSchoolFestivals(args.schoolId) }
        }
        adapter.submitList(items)

        binding.llcSchoolSocialMedia.removeAllViews()

        uiState.schoolInfo.socialMedia.forEach { media ->
            with(ItemMediaBinding.inflate(layoutInflater, binding.llcSchoolSocialMedia, false)) {
                imageUrl = media.logoUrl
                ivImage.setOnClickListener { startBrowser(media.url) }
                binding.llcSchoolSocialMedia.addView(ivImage)
            }
        }
    }

    private fun handleEvent(event: SchoolDetailEvent) = when (event) {
        is SchoolDetailEvent.ShowArtistDetail -> {
            findNavController().navigate(
                SchoolDetailFragmentDirections.actionSchoolDetailFragmentToArtistDetailFragment(
                    event.artistId,
                ),
            )
        }

        is SchoolDetailEvent.ShowFestivalDetail -> {
            findNavController().navigate(
                SchoolDetailFragmentDirections.actionSchoolDetailFragmentToFestivalDetailFragment(
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
