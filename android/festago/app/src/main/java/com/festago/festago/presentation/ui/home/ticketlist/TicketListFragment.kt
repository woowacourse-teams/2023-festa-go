package com.festago.festago.presentation.ui.home.ticketlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val vm: TicketListViewModel by viewModels {
        TicketListViewModel.TicketListViewModelFactory(
            TicketDefaultRepository(
                ticketRetrofitService = RetrofitClient.instance.ticketRetrofitService,
            ),
            analyticsHelper = FirebaseAnalyticsHelper,
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
        initActivityResult()
        initRefresh()
    }

    private fun initRefresh() {
        binding.srlTicketList.setOnRefreshListener {
            vm.loadCurrentTickets()
        }
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
            -> binding.srlTicketList.isRefreshing = false

            is TicketListUiState.Success -> {
                adapter.submitList(uiState.tickets)
                binding.srlTicketList.isRefreshing = false
            }
        }
    }

    private fun handleEvent(event: TicketListEvent) {
        when (event) {
            is TicketListEvent.ShowTicketEntry -> showTicketEntry(event)
        }
    }

    private fun showTicketEntry(event: TicketListEvent.ShowTicketEntry) {
        resultLauncher.launch(
            TicketEntryActivity.getIntent(
                context = requireContext(),
                ticketId = event.ticketId,
            ),
        )
    }

    private fun initActivityResult() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == TicketEntryActivity.RESULT_OK) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fcv_home_container, TicketListFragment()).commit()
                }
            }
    }

    private fun initView() {
        adapter = TicketListAdapter(vm)
        binding.rvTicketList.adapter = adapter
        vm.loadCurrentTickets()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
