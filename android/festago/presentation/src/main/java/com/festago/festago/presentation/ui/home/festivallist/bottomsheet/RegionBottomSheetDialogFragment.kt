package com.festago.festago.presentation.ui.home.festivallist.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.festago.festago.domain.model.festival.SchoolRegion
import com.festago.festago.presentation.databinding.FragmentRegionBottomSheetBinding
import com.festago.festago.presentation.ui.home.festivallist.uistate.SchoolRegionUiState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegionBottomSheetDialogFragment() : BottomSheetDialogFragment() {

    private var _binding: FragmentRegionBottomSheetBinding? = null
    private val binding: FragmentRegionBottomSheetBinding get() = _binding!!

    private lateinit var listener: OnRegionSelectListener
    private lateinit var items: List<SchoolRegionUiState>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionBottomSheetBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        binding.rvRegionList.adapter = RegionAdapter(
            items = items,
            onRegionSelect = listener::onRegionSelect
        ) { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnRegionSelectListener {
        fun onRegionSelect(region: SchoolRegion)
    }

    companion object {
        fun newInstance(
            items: List<SchoolRegionUiState>,
            listener: OnRegionSelectListener,
        ): RegionBottomSheetDialogFragment = RegionBottomSheetDialogFragment().apply {
            this.listener = listener
            this.items = items
        }
    }
}
