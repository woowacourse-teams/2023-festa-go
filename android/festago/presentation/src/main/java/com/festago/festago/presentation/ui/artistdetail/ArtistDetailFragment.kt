package com.festago.festago.presentation.ui.artistdetail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentArtistDetailBinding
import com.festago.festago.presentation.ui.artistdetail.festival.ArtistDetailAdapter
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistDetailUiState
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistDetailFragment : Fragment() {
    private var _binding: FragmentArtistDetailBinding? = null
    private val binding get() = _binding!!

    private val vm: ArtistDetailViewModel by viewModels()

    private val adapter = ArtistDetailAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
    }

    private fun initView() {
        binding.rvToDoList.adapter = adapter

        this.activity?.window?.apply {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        }
    }

    private fun initObserve() {
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiState.collect {
                updateUi(it)
            }
        }
    }

    private fun updateUi(uiState: ArtistDetailUiState) = when (uiState) {
        is ArtistDetailUiState.Loading,
        is ArtistDetailUiState.Error -> Unit

        is ArtistDetailUiState.Success -> {
            binding.uiState = uiState
            adapter.submitList(uiState.stages)
        }
    }
}