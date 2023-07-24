package com.festago.festago.presentation.ui.home.festivallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.festago.festago.R
import com.festago.festago.databinding.FragmentFestivalListBinding

class FestivalListFragment : Fragment(R.layout.fragment_festival_list) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentFestivalListBinding.inflate(inflater)
        return binding.root
    }
}
