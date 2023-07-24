package com.festago.festago.presentation.ui.home.festivallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.festago.festago.R
import com.festago.festago.databinding.FragmentFestivalListBinding

class FestivalListFragment : Fragment(R.layout.fragment_festival_list) {

    private var _binding: FragmentFestivalListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFestivalListBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
