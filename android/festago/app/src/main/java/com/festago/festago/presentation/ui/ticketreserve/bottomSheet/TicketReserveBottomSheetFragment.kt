package com.festago.festago.presentation.ui.ticketreserve.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.festago.festago.R
import com.festago.festago.databinding.FragmentTicketReserveBottomSheetBinding
import com.festago.festago.presentation.model.ReservationStageUiModel
import com.festago.festago.presentation.util.getParcelableCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.format.DateTimeFormatter
import java.util.Locale

class TicketReserveBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentTicketReserveBottomSheetBinding

    private val ticketTypeAdapter = TicketReserveBottomSheetAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTicketReserveBottomSheetBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        val dateFormatter = DateTimeFormatter.ofPattern(
            binding.root.context.getString(R.string.ticket_reserve_tv_start_time),
            Locale.KOREA,
        )

        arguments?.getParcelableCompat<ReservationStageUiModel>(KET_ITEM)?.let {
            binding.reservationStage = it
            binding.tvDate.text = it.startTime.format(dateFormatter)
            ticketTypeAdapter.submitList(it.reservationTickets)
        }

        binding.rvTicketTypes.adapter = ticketTypeAdapter

        return binding.root
    }

    companion object {
        private const val KET_ITEM = "KET_ITEM"

        fun newInstance(item: ReservationStageUiModel) = TicketReserveBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KET_ITEM, item)
            }
        }
    }
}
