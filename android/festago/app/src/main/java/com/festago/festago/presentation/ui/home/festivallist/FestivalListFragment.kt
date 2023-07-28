package com.festago.festago.presentation.ui.home.festivallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.R
import com.festago.festago.data.repository.FestivalDefaultRepository
import com.festago.festago.databinding.FragmentFestivalListBinding
import com.festago.festago.presentation.ui.home.festivallist.FestivalListViewModel.FestivalListViewModelFactory
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveActivity

class FestivalListFragment : Fragment(R.layout.fragment_festival_list) {

    private var _binding: FragmentFestivalListBinding? = null
    private val binding get() = _binding!!

    private val vm: FestivalListViewModel by viewModels {
        FestivalListViewModelFactory(
            FestivalDefaultRepository(),
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
    }

    private fun updateUi(uiState: FestivalListUiState) {
        when (uiState) {
            is FestivalListUiState.Loading, is FestivalListUiState.Error -> Unit

            is FestivalListUiState.Success -> {
                adapter.submitList(uiState.festivals)
            }
        }
    }

    private fun handleEvent(event: FestivalListEvent) {
        when (event) {
            is FestivalListEvent.OpenTicketReserve -> {
                startActivity(TicketReserveActivity.getIntent(requireContext(), event.festivalId))
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
