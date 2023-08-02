package com.festago.festago.presentation.ui.ticketreserve.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.festago.festago.databinding.FragmentTicketReserveBottomSheetBinding
import com.festago.festago.presentation.model.ReservationTicketUiModel
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel
import com.festago.festago.presentation.util.getParcelableArrayCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TicketReserveBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTicketReserveBottomSheetBinding? = null
    private val binding: FragmentTicketReserveBottomSheetBinding get() = _binding!!

    private lateinit var vm: TicketReserveViewModel

    private val ticketTypeAdapter = TicketReserveBottomSheetAdapter()

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
        arguments?.getParcelableArrayCompat<ReservationTicketUiModel>(KET_ITEM)?.let {
            ticketTypeAdapter.submitList(it.map(::TicketReserveBottomItem))
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
        private const val KET_ITEM = "KET_ITEM"

        fun newInstance(items: List<ReservationTicketUiModel>) =
            TicketReserveBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArray(KET_ITEM, items.toTypedArray())
                }
            }
    }
}
