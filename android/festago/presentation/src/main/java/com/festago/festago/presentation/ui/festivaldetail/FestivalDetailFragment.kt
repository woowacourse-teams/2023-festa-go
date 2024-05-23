package com.festago.festago.presentation.ui.festivaldetail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.festago.festago.domain.model.social.SocialMedia
import com.festago.festago.domain.model.social.SocialMediaType
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.FragmentFestivalDetailBinding
import com.festago.festago.presentation.databinding.ItemMediaBinding
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailArgs
import com.festago.festago.presentation.ui.bindingadapter.setImage
import com.festago.festago.presentation.ui.festivaldetail.adapter.stage.StageListAdapter
import com.festago.festago.presentation.ui.festivaldetail.uiState.FestivalDetailUiState
import com.festago.festago.presentation.ui.schooldetail.SchoolDetailArgs
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class FestivalDetailFragment : Fragment() {
    private var _binding: FragmentFestivalDetailBinding? = null
    private val binding get() = _binding!!

    private val vm: FestivalDetailViewModel by viewModels()

    private val args: FestivalDetailFragmentArgs by navArgs()

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
        loadFestivalDetail()
        initStageAdapter()
        initButton()
    }

    private fun loadFestivalDetail() {
        binding.ivFestivalPoster.setImage(args.festival.posterImageUrl)
        binding.ivFestivalBackground.setImage(args.festival.posterImageUrl)
        binding.tvFestivalName.text = args.festival.name
        val delayTimeMillis = resources.getInteger(R.integer.nav_Anim_time).toLong()
        vm.loadFestivalDetail(args.festival.id, delayTimeMillis)
    }

    private fun initStageAdapter() {
        adapter = StageListAdapter()
        binding.rvStageList.adapter = adapter
    }

    private fun initButton() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
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

    private fun updateUi(uiState: FestivalDetailUiState) {
        when (uiState) {
            is FestivalDetailUiState.Loading -> Unit
            is FestivalDetailUiState.Success -> handleSuccess(uiState)
            is FestivalDetailUiState.Error -> handleError(uiState)
        }
    }

    private fun handleSuccess(uiState: FestivalDetailUiState.Success) {
        binding.successUiState = uiState
        binding.ivBookmark.isSelected = uiState.bookmarked
        binding.tvFestivalDDay.setFestivalDDay(uiState.festival.startDate, uiState.festival.endDate)
        binding.ivFestivalPoster.setImage(uiState.festival.posterImageUrl)
        binding.ivFestivalBackground.setImage(uiState.festival.posterImageUrl)
        binding.tvFestivalName.text = uiState.festival.name
        adapter.submitList(uiState.stages)
        binding.llcFestivalSocialMedia.removeAllViews()
        uiState.festival.socialMedias.forEach { media ->
            with(ItemMediaBinding.inflate(layoutInflater, binding.llcFestivalSocialMedia, false)) {
                ivImage.setImageResource(findMediaRes(media))
                ivImage.setOnClickListener { startBrowser(media.url) }
                binding.llcFestivalSocialMedia.addView(ivImage)
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

    private fun handleError(uiState: FestivalDetailUiState.Error) {
        binding.refreshListener = { uiState.refresh(args.festival.id) }
    }

    private fun TextView.setFestivalDDay(startDate: LocalDate, endDate: LocalDate) {
        when {
            LocalDate.now() in startDate..endDate -> {
                text = context.getString(R.string.tv_dday_in_progress)
                setTextColor(context.getColor(R.color.secondary_pink_01))
                background = AppCompatResources.getDrawable(
                    context,
                    R.drawable.bg_festival_list_dday_in_progress,
                )
            }

            LocalDate.now() < startDate -> {
                val dDay = LocalDate.now().toEpochDay() - startDate.toEpochDay()
                val backgroundColor = if (dDay >= -7L) {
                    context.getColor(R.color.secondary_pink_01)
                } else {
                    context.getColor(R.color.contents_gray_07)
                }
                setBackgroundColor(backgroundColor)
                setTextColor(context.getColor(R.color.background_gray_01))
                text = context.getString(R.string.tv_dday_format, dDay.toString())
            }

            else -> {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(context.getColor(R.color.background_gray_01))
                background = AppCompatResources.getDrawable(
                    context,
                    R.drawable.bg_festival_detail_dday_end,
                )
                text = context.getString(R.string.tv_dday_end)
            }
        }
    }

    private fun startBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun handleEvent(event: FestivalDetailEvent) {
        when (event) {
            is FestivalDetailEvent.ShowArtistDetail -> {
                findNavController().navigate(
                    FestivalDetailFragmentDirections.actionFestivalDetailFragmentToArtistDetailFragment(
                        with(event.artist) { ArtistDetailArgs(id, name, imageUrl) },
                    ),
                )
            }

            is FestivalDetailEvent.ShowSchoolDetail -> {
                findNavController().navigate(
                    FestivalDetailFragmentDirections.actionFestivalDetailFragmentToSchoolDetailFragment(
                        with(event.school) { SchoolDetailArgs(id, name, imageUrl) },
                    ),
                )
            }

            is FestivalDetailEvent.BookmarkSuccess -> {
                Toast.makeText(
                    requireContext(),
                    if (event.isBookmarked) {
                        getString(R.string.festival_detail_bookmark_success)
                    } else {
                        getString(R.string.festival_detail_bookmark_cancel)
                    },
                    Toast.LENGTH_SHORT,
                ).show()
            }

            is FestivalDetailEvent.BookmarkFailure -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
