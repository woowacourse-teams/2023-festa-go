package com.festago.festago.presentation.ui.home.festivallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.festago.festago.R
import com.festago.festago.databinding.FragmentFestivalListBinding
import com.festago.festago.model.FestivalFilter
import com.festago.festago.presentation.ui.home.ticketlist.TicketListFragment
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveActivity
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FestivalListFragment : Fragment(R.layout.fragment_festival_list) {

    private var _binding: FragmentFestivalListBinding? = null
    private val binding get() = _binding!!

    private val vm: FestivalListViewModel by viewModels()

    private lateinit var adapter: FestivalListAdapter
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
        repeatOnStarted(viewLifecycleOwner) {
            vm.event.collect {
                handleEvent(it)
            }
        }
    }

    private val Int.dp: Int get() = (this / resources.displayMetrics.density).toInt()

    private fun initView() {
        adapter = FestivalListAdapter()
        binding.rvFestivalList.adapter = adapter

        initFestivalListSpanSize()
        initRefresh()
        initFestivalFilters()
        loadFestivalsBy(binding.cgFilterOption.checkedChipId)
    }

    private fun initFestivalListSpanSize() {
        binding.rvFestivalList.layoutManager.apply {
            if (this is GridLayoutManager) {
                val spanSize = (resources.displayMetrics.widthPixels.dp / 160)
                spanCount = when {
                    spanSize < 2 -> 2
                    spanSize > 4 -> 4
                    else -> spanSize
                }
            }
        }
    }

    private fun initRefresh() {
        binding.srlFestivalList.setOnRefreshListener {
            loadFestivalsBy(binding.cgFilterOption.checkedChipId)
            binding.srlFestivalList.isRefreshing = false
        }
    }

    private fun initFestivalFilters() {
        binding.cgFilterOption.setOnCheckedStateChangeListener { group, _ ->
            loadFestivalsBy(checkedChipId = group.checkedChipId)
        }
    }

    private fun loadFestivalsBy(checkedChipId: Int) {
        when (checkedChipId) {
            R.id.chipProgress -> vm.loadFestivals(FestivalFilter.PROGRESS)
            R.id.chipPlanned -> vm.loadFestivals(FestivalFilter.PLANNED)
            R.id.chipEnd -> vm.loadFestivals(FestivalFilter.END)
        }
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
        adapter.submitList(uiState.festivals)
    }

    private fun handleEvent(event: FestivalListEvent) {
        when (event) {
            is FestivalListEvent.ShowTicketReserve -> {
                removeTicketListFragment()
                startActivity(TicketReserveActivity.getIntent(requireContext(), event.festivalId))
            }
        }
    }

    private fun removeTicketListFragment() {
        parentFragmentManager.findFragmentByTag(TicketListFragment::class.java.name)?.let {
            parentFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
