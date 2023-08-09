package com.festago.festago.presentation.ui.home.festivallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.R
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.data.RetrofitClient
import com.festago.festago.data.repository.FestivalDefaultRepository
import com.festago.festago.databinding.FragmentFestivalListBinding
import com.festago.festago.presentation.ui.home.festivallist.FestivalListViewModel.FestivalListViewModelFactory
import com.festago.festago.presentation.ui.home.ticketlist.TicketListFragment
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveActivity

class FestivalListFragment : Fragment(R.layout.fragment_festival_list) {

    private var _binding: FragmentFestivalListBinding? = null
    private val binding get() = _binding!!

    private val vm: FestivalListViewModel by viewModels {
        FestivalListViewModelFactory(
            FestivalDefaultRepository(RetrofitClient.instance.festivalRetrofitService),
            FirebaseAnalyticsHelper.getInstance(),
        )
    }

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
        vm.uiState.observe(viewLifecycleOwner) {
            binding.uiState = it
            updateUi(it)
        }
        vm.event.observe(viewLifecycleOwner) {
            handleEvent(it)
        }
    }

    private fun initView() {
        adapter = FestivalListAdapter(vm)
        binding.rvFestivalList.adapter = adapter
        vm.loadFestivals()

        binding.srlFestivalList.setOnRefreshListener {
            vm.loadFestivals()
        }
    }

    private fun updateUi(uiState: FestivalListUiState) {
        when (uiState) {
            is FestivalListUiState.Loading,
            is FestivalListUiState.Error,
            -> binding.srlFestivalList.isRefreshing = false

            is FestivalListUiState.Success -> handleSuccess(uiState)
        }
    }

    private fun handleSuccess(uiState: FestivalListUiState.Success) {
        adapter.submitList(uiState.festivals)
        binding.srlFestivalList.isRefreshing = false
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
