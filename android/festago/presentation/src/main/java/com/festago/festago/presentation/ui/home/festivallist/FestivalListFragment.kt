package com.festago.festago.presentation.ui.home.festivallist

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.domain.model.festival.SchoolRegion
import com.festago.festago.presentation.databinding.FragmentFestivalListBinding
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailArgs
import com.festago.festago.presentation.ui.festivaldetail.FestivalDetailArgs
import com.festago.festago.presentation.ui.home.festivallist.FestivalListFragmentDirections.actionFestivalListFragmentToSearchFragment
import com.festago.festago.presentation.ui.home.festivallist.bottomsheet.RegionBottomSheetDialogFragment
import com.festago.festago.presentation.ui.home.festivallist.festival.FestivalListAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalEmptyItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalListUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalMoreItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalTabUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.SchoolRegionUiState
import com.festago.festago.presentation.ui.notificationlist.NotificationListActivity
import com.festago.festago.presentation.util.repeatOnStarted
import com.festago.festago.presentation.util.setOnApplyWindowInsetsCompatListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FestivalListFragment : Fragment() {
    private var _binding: FragmentFestivalListBinding? = null
    private val binding get() = _binding!!

    private lateinit var festivalListAdapter: FestivalListAdapter

    private val vm: FestivalListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFestivalListBinding.inflate(inflater)
        binding.root.setOnApplyWindowInsetsCompatListener { view, windowInsets ->
            val statusBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBarInsets.top, 0, 0)
            windowInsets
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserve()
        initView()
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
        vm.initFestivalList()
        initViewPager()
        initRecyclerViewDecoration()
        initRefresh()
    }

    private fun initRefresh() {
        binding.srlFestivalList.setOnRefreshListener {
            vm.initFestivalList()
            binding.srlFestivalList.isRefreshing = false
        }
        binding.srlFestivalList.setDistanceToTriggerSync(400)
        binding.ivSearch.setOnClickListener {
            showSearch()
        }
        binding.ivAlarm.setOnClickListener {
            showNotificationList()
        }
    }

    private fun initViewPager() {
        festivalListAdapter = FestivalListAdapter(
            onArtistClick = { artist ->
                findNavController().navigate(
                    FestivalListFragmentDirections.actionFestivalListFragmentToArtistDetailFragment(
                        with(artist) { ArtistDetailArgs(id, name, imageUrl) },
                    ),
                )
            },
        )
        binding.rvFestivalList.adapter = festivalListAdapter
    }

    private fun initRecyclerViewDecoration() {
        binding.rvFestivalList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State,
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
                    outRect.bottom = 32.dpToPx
                }
            }

            private val Int.dpToPx: Int
                get() = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    this.toFloat(),
                    Resources.getSystem().displayMetrics,
                ).toInt()
        })
    }

    private fun updateUi(uiState: FestivalListUiState) {
        when (uiState) {
            is FestivalListUiState.Loading -> Unit
            is FestivalListUiState.Success -> handleSuccess(uiState)
            is FestivalListUiState.Error -> handleError(uiState)
        }
    }

    private fun handleEvent(event: FestivalListEvent) {
        when (event) {
            is FestivalListEvent.ShowFestivalDetail -> {
                findNavController().navigate(
                    FestivalListFragmentDirections.actionFestivalListFragmentToFestivalDetailFragment(
                        with(event.festival) { FestivalDetailArgs(id, name, imageUrl) },
                    ),
                )
            }
        }
    }

    private fun handleSuccess(uiState: FestivalListUiState.Success) {
        val items = uiState.getItems()
        festivalListAdapter.submitList(items)
        binding.rvFestivalList.itemAnimator = null
    }

    private fun FestivalListUiState.Success.getItems(): List<Any> {
        val schoolRegions = SchoolRegion.values().map {
            SchoolRegionUiState(it, it == this.schoolRegion)
        }
        val dialog = createRegionDialog(schoolRegions)

        return mutableListOf<Any>().apply {
            if (popularFestivalUiState.festivals.isNotEmpty()) {
                add(popularFestivalUiState)
            }
            add(
                FestivalTabUiState(
                    selectedFilter = festivalFilter,
                    selectedRegion = schoolRegion,
                    onFilterSelected = { vm.loadFestivals(it, schoolRegion) },
                ) {
                    dialog.show(
                        parentFragmentManager,
                        RegionBottomSheetDialogFragment::class.java.name,
                    )
                },
            )
            addAll(festivals)
            if (!isLastPage) {
                add(FestivalMoreItemUiState(::requestMoreFestival))
            } else if (festivals.isEmpty()) add(FestivalEmptyItemUiState(festivalFilter.tabPosition))
        }.toList()
    }

    private fun handleError(uiState: FestivalListUiState.Error) {
        binding.refreshListener = uiState.refresh
    }

    private fun requestMoreFestival() {
        val festivalListUiState = vm.uiState.value as? FestivalListUiState.Success ?: return
        if (festivalListUiState.isLastPage) return
        if (festivalListUiState.festivals.isEmpty()) return
        vm.loadFestivals(
            schoolRegion = festivalListUiState.schoolRegion,
            isLoadMore = true,
        )
    }

    private fun FestivalListUiState.Success.createRegionDialog(
        schoolRegions: List<SchoolRegionUiState>,
    ): RegionBottomSheetDialogFragment {
        val factory =
            RegionBottomSheetDialogFragment.Companion.RegionBottomSheetDialogFragmentFactory(
                items = schoolRegions,
                listener = object : RegionBottomSheetDialogFragment.OnRegionSelectListener {
                    override fun onRegionSelect(region: SchoolRegion) {
                        vm.loadFestivals(festivalFilter, region)
                    }
                }
            )
        val fragment = factory.instantiate(
            classLoader = RegionBottomSheetDialogFragment::class.java.classLoader!!,
            className = RegionBottomSheetDialogFragment::class.java.name,
        ) as RegionBottomSheetDialogFragment

        return fragment
    }

    private fun showSearch() {
        findNavController().navigate(actionFestivalListFragmentToSearchFragment())
    }

    private fun showNotificationList() {
        startActivity(NotificationListActivity.getIntent(requireContext()))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
