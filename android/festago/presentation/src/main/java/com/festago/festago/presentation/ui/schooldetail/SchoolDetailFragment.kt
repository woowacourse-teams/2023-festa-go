package com.festago.festago.presentation.ui.schooldetail

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
import com.festago.festago.domain.model.social.SocialMedia
import com.festago.festago.domain.model.social.SocialMediaType
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.FragmentSchoolDetailBinding
import com.festago.festago.presentation.databinding.ItemMediaBinding
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailArgs
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
        val delayTimeMillis = resources.getInteger(R.integer.nav_Anim_time).toLong()
        vm.loadSchoolDetail(args.school.id, delayTimeMillis)
    }

    private fun initButton() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun updateUi(uiState: SchoolDetailUiState) {
        when (uiState) {
            is SchoolDetailUiState.Loading -> Unit
            is SchoolDetailUiState.Success -> handleSuccess(uiState)
            is SchoolDetailUiState.Error -> handleError(uiState)
        }
    }

    private fun handleSuccess(uiState: SchoolDetailUiState.Success) {
        binding.successUiState = uiState
        binding.ivBookmark.isSelected = uiState.bookmarked
        binding.tvSchoolName.text = uiState.schoolInfo.schoolName
        val items: List<Any> = if (uiState.isLast) {
            uiState.festivals
        } else {
            uiState.festivals + MoreItemUiState { vm.loadMoreSchoolFestivals(args.school.id) }
        }
        adapter.submitList(items)

        binding.llcSchoolSocialMedia.removeAllViews()

        uiState.schoolInfo.socialMedia.forEach { media ->
            with(ItemMediaBinding.inflate(layoutInflater, binding.llcSchoolSocialMedia, false)) {
                if (media.type == SocialMediaType.NONE) return@with
                ivImage.setImageResource(findMediaRes(media))
                ivImage.setOnClickListener { startBrowser(media.url) }
                binding.llcSchoolSocialMedia.addView(ivImage)
            }
        }
    }

    private fun findMediaRes(media: SocialMedia): Int {
        val res = when (media.type) {
            SocialMediaType.INSTAGRAM -> R.drawable.ic_instagram
            SocialMediaType.FACEBOOK -> R.drawable.ic_facebook
            SocialMediaType.YOUTUBE -> R.drawable.ic_youtube
            SocialMediaType.X -> R.drawable.ic_x
            else -> R.drawable.bg_festago_default
        }
        return res
    }

    private fun handleError(uiState: SchoolDetailUiState.Error) {
        binding.refreshListener = { uiState.refresh(args.school.id) }
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

        is SchoolDetailEvent.BookmarkSuccess -> {
            Toast.makeText(
                requireContext(),
                if (event.isBookmarked) {
                    getString(R.string.school_detail_bookmark_success)
                } else {
                    getString(R.string.school_detail_bookmark_cancel)
                },
                Toast.LENGTH_SHORT,
            ).show()
        }

        is SchoolDetailEvent.BookmarkFailure -> {
            Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
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
