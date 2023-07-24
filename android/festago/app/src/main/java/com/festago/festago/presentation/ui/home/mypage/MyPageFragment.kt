package com.festago.festago.presentation.ui.home.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.festago.festago.R
import com.festago.festago.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment(R.layout.fragment_my_page) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMyPageBinding.inflate(inflater)
        return binding.root
    }
}
