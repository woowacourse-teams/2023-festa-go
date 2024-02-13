package com.festago.festago.presentation.ui.home.festivallist

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.FragmentFestivalListBinding
import com.festago.festago.presentation.ui.home.festivallist.festival.FestivalListAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalListUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalTabUiState
import com.festago.festago.presentation.util.repeatOnStarted
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
    }

    private fun initView() {
        initViewPager()
        initRecyclerView()
        initRefresh()
    }

    private fun initRefresh() {
        binding.srlFestivalList.setOnRefreshListener {
            vm.loadFestivals(festivalFilterUiState = null)
            binding.srlFestivalList.isRefreshing = false
        }
    }

    private fun initViewPager() {
        festivalListAdapter = FestivalListAdapter()
        binding.rvFestivalList.adapter = festivalListAdapter
    }

    private fun initRecyclerView() {
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
            is FestivalListUiState.Loading,
            is FestivalListUiState.Error,
            -> Unit

            is FestivalListUiState.Success -> handleSuccess(uiState)
        }
    }

    private fun handleSuccess(uiState: FestivalListUiState.Success) {
        festivalListAdapter.submitList(
            listOf(
                uiState,
                FestivalTabUiState {
                    val festivalFilter = when (it) {
                        0 -> FestivalFilterUiState.PLANNED
                        1 -> FestivalFilterUiState.PROGRESS
                        else -> FestivalFilterUiState.PLANNED
                    }
                    vm.loadFestivals(festivalFilter)
                },
            ) + uiState.festivals,
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
