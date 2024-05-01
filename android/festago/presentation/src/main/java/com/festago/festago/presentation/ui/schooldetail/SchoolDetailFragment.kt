package com.festago.festago.presentation.ui.schooldetail

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
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.FragmentSchoolDetailBinding
import com.festago.festago.presentation.databinding.ItemMediaBinding
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailArgs
import com.festago.festago.presentation.ui.bindingadapter.setImage
import com.festago.festago.presentation.ui.festivaldetail.FestivalDetailArgs
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
        initView()
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

    private fun initView() {
        adapter = SchoolFestivalListAdapter()
        binding.rvFestivalList.adapter = adapter
        loadSchoolDetail()
        initButton()
    }

    private fun loadSchoolDetail() {
        binding.tvSchoolName.text = args.school.name
        binding.ivSchoolLogoImage.setImage(args.school.profileImageUrl)
        val delayTimeMillis = resources.getInteger(R.integer.nav_Anim_time).toLong()
        vm.loadSchoolDetail(args.school.id, delayTimeMillis)
    }

    private fun initButton() {
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
        binding.tvSchoolName.text = uiState.schoolInfo.schoolName
        binding.ivSchoolLogoImage.setImage(uiState.schoolInfo.logoUrl)
        val items: List<Any> = if (uiState.isLast) {
            uiState.festivals
        } else {
            uiState.festivals + MoreItemUiState { vm.loadMoreSchoolFestivals(args.school.id) }
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
                    with(event.artist) { ArtistDetailArgs(id, name, imageUrl) },
                ),
            )
        }

        is SchoolDetailEvent.ShowFestivalDetail -> {
            findNavController().navigate(
                SchoolDetailFragmentDirections.actionSchoolDetailFragmentToFestivalDetailFragment(
                    with(event.festival) { FestivalDetailArgs(id, name, imageUrl) },
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
