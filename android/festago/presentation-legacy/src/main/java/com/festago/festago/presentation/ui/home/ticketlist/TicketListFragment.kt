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
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.FragmentTicketListBinding
import com.festago.festago.presentation.ui.ticketentry.TicketEntryActivity
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketListFragment : Fragment(R.layout.fragment_ticket_list) {

    private var _binding: FragmentTicketListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TicketListAdapter

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val vm: TicketListViewModel by viewModels()

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
    }

    private fun initObserve() {
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiState.collect {
                binding.uiState = it
                updateUi(it)
            }
        }
        repeatOnStarted(viewLifecycleOwner) {
            vm.event.collect { event ->
                handleEvent(event)
            }
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
        adapter = TicketListAdapter()
        binding.rvTicketList.adapter = adapter
        vm.loadCurrentTickets()
        initRefresh()
    }

    private fun initRefresh() {
        binding.srlTicketList.setOnRefreshListener {
            vm.loadCurrentTickets()
            binding.srlTicketList.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
