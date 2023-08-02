package com.festago.festago.presentation.ui.ticketreserve.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.festago.festago.databinding.FragmentTicketReserveBottomSheetBinding
import com.festago.festago.presentation.model.ReservationStageUiModel
import com.festago.festago.presentation.model.ReservationTicketUiModel
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel
import com.festago.festago.presentation.util.getParcelableArrayCompat
import com.festago.festago.presentation.util.getParcelableCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TicketReserveBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTicketReserveBottomSheetBinding? = null
    private val binding: FragmentTicketReserveBottomSheetBinding get() = _binding!!

    private lateinit var vm: TicketReserveViewModel

    private val ticketTypeAdapter = TicketReserveBottomSheetAdapter { ticketId ->
        binding.selectedTicketTypeId = ticketId
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
        binding.vm = vm
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.apply {
            getParcelableCompat<ReservationStageUiModel>(KEY_STAGE)?.let { stage ->
                binding.stage = stage
            }
            getParcelableArrayCompat<ReservationTicketUiModel>(KEY_ITEM)?.let {
                ticketTypeAdapter.submitList(it.map(::TicketReserveBottomItem))
            }
        }

        initView()
    }

    private fun initView() {
        binding.rvTicketTypes.adapter = ticketTypeAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_STAGE = "KEY_STAGE"
        private const val KEY_ITEM = "KEY_ITEM"

        fun newInstance(
            stage: ReservationStageUiModel,
            items: List<ReservationTicketUiModel>,
        ) = TicketReserveBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_STAGE, stage)
                putParcelableArray(KEY_ITEM, items.toTypedArray())
            }
        }
    }
}
