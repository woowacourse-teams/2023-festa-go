package com.festago.festago.presentation.ui.home.ticketlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.festago.festago.R
import com.festago.festago.databinding.FragmentTicketListBinding

class TicketListFragment : Fragment(R.layout.fragment_ticket_list) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentTicketListBinding.inflate(inflater)
        return binding.root
    }
}
