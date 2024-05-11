package com.festago.festago.presentation.ui.home.festivallist.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.festago.festago.domain.model.festival.SchoolRegion
import com.festago.festago.presentation.databinding.FragmentRegionBottomSheetBinding
import com.festago.festago.presentation.ui.home.festivallist.uistate.SchoolRegionUiState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RegionBottomSheetDialogFragment(
    private val items: List<SchoolRegionUiState>,
    private val listener: OnRegionSelectListener,
) : BottomSheetDialogFragment() {

    private var _binding: FragmentRegionBottomSheetBinding? = null
    private val binding: FragmentRegionBottomSheetBinding get() = _binding!!

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
        class RegionBottomSheetDialogFragmentFactory(
            private val items: List<SchoolRegionUiState>,
            private val listener: OnRegionSelectListener
        ) : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when (className) {
                    RegionBottomSheetDialogFragment::class.java.name -> RegionBottomSheetDialogFragment(
                        items = items,
                        listener = listener
                    )

                    else -> super.instantiate(classLoader, className)
                }
            }
        }
    }
}

