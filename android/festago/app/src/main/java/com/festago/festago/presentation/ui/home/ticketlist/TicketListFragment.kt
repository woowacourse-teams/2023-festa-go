package com.festago.festago.presentation.ui.home.ticketlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.R
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.data.RetrofitClient
import com.festago.festago.data.repository.TicketDefaultRepository
import com.festago.festago.databinding.FragmentTicketListBinding
import com.festago.festago.presentation.ui.ticketentry.TicketEntryActivity

class TicketListFragment : Fragment(R.layout.fragment_ticket_list) {

    private var _binding: FragmentTicketListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TicketListAdapter

    private val vm: TicketListViewModel by viewModels {
        TicketListViewModel.TicketListViewModelFactory(
            TicketDefaultRepository(
                ticketRetrofitService = RetrofitClient.getInstance().ticketRetrofitService,
            ),
            analyticsHelper = FirebaseAnalyticsHelper.getInstance(),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTicketListBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
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
        vm.event.observe(viewLifecycleOwner) { event ->
            handleEvent(event)
        }
    }

    private fun updateUi(uiState: TicketListUiState) {
        when (uiState) {
            is TicketListUiState.Loading,
            is TicketListUiState.Error,
            -> Unit

            is TicketListUiState.Success -> {
                adapter.submitList(uiState.tickets)
            }
        }
    }

    private fun handleEvent(event: TicketListEvent) {
        when (event) {
            is TicketListEvent.ShowTicketEntry -> showTicketEntry(event)
        }
    }

    private fun showTicketEntry(event: TicketListEvent.ShowTicketEntry) = startActivity(
        TicketEntryActivity.getIntent(context = requireContext(), ticketId = event.ticketId),
    )

    private fun initView() {
        adapter = TicketListAdapter(vm)
        binding.rvTicketList.adapter = adapter
        vm.loadTickets()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
