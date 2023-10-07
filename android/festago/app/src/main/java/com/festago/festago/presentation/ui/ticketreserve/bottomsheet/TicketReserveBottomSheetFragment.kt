package com.festago.festago.presentation.ui.ticketreserve.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.festago.festago.databinding.FragmentTicketReserveBottomSheetBinding
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel
import com.festago.festago.presentation.util.getParcelableArrayListCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketReserveBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTicketReserveBottomSheetBinding? = null
    private val binding: FragmentTicketReserveBottomSheetBinding get() = _binding!!

    private lateinit var vm: TicketReserveViewModel

    private val ticketTypeAdapter = TicketReserveBottomSheetAdapter { ticketId ->
        binding.selectedTicketTypeId = ticketId
        binding.btnReserveTicket.isEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelProvider = ViewModelProvider(requireActivity())
        vm = viewModelProvider[TicketReserveViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTicketReserveBottomSheetBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.apply {
            getString(KEY_STAGE_START_TIME)?.let { startTime ->
                binding.stageStartTime = startTime
            }
            getParcelableArrayListCompat<BottomSheetReservationTicketArg>(KEY_ITEMS)?.let {
                Log.d("asdf", it.toString())
                ticketTypeAdapter.submitList(it.map(::TicketReserveBottomItem))
            }
        }

        initView()
    }

    private fun initView() {
        binding.rvTicketTypes.adapter = ticketTypeAdapter
        val onReserve: (Int) -> Unit = { id -> vm.reserveTicket(id) }
        binding.onReserve = onReserve
        binding.btnReserveTicket.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_STAGE_START_TIME = "KEY_STAGE_START_TIME"
        private const val KEY_ITEMS = "KEY_ITEMS"

        fun newInstance(
            stageStartTime: String,
            items: List<BottomSheetReservationTicketArg>,
        ) = TicketReserveBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_STAGE_START_TIME, stageStartTime)
                putParcelableArrayList(KEY_ITEMS, items as ArrayList)
            }
        }
    }
}
