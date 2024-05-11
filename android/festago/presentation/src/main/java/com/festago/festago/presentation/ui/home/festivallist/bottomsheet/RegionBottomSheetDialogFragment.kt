package com.festago.festago.presentation.ui.home.festivallist.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.festago.festago.domain.model.festival.SchoolRegion
import com.festago.festago.presentation.databinding.FragmentRegionBottomSheetBinding
import com.festago.festago.presentation.ui.home.festivallist.uistate.SchoolRegionUiState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RegionBottomSheetDialogFragment() : BottomSheetDialogFragment() {

    private var _binding: FragmentRegionBottomSheetBinding? = null
    private val binding: FragmentRegionBottomSheetBinding get() = _binding!!

    var items: List<SchoolRegionUiState>? = null
    var listener: OnRegionSelectListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentRegionBottomSheetBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val schoolRegions = items ?: return
        val onRegionSelectListener = listener ?: return

        binding.rvRegionList.adapter = RegionAdapter(
            items = schoolRegions,
            onRegionSelect = onRegionSelectListener::onRegionSelect,
        ) { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnRegionSelectListener {
        fun onRegionSelect(region: SchoolRegion)
    }
}
