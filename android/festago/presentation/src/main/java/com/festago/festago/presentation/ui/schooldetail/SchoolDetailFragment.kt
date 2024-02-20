package com.festago.festago.presentation.ui.schooldetail

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentSchoolDetailBinding
import com.festago.festago.presentation.ui.schooldetail.uistate.SchoolDetailUiState
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchoolDetailFragment : Fragment() {

    private var _binding: FragmentSchoolDetailBinding? = null
    private val binding get() = _binding!!

    private val vm: SchoolDetailViewModel by viewModels()

    private lateinit var adapter: SchoolFestivalListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSchoolDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val schoolId = requireArguments().getLong(SCHOOL_ID_KEY)
        initView(schoolId)
        initObserve()
    }

    private fun initObserve() {
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiState.collect {
                binding.uiState = it
                updateUi(it)
            }
        }
    }

    private fun initView(schoolId: Long) {
        adapter = SchoolFestivalListAdapter()
        binding.rvFestivalList.adapter = adapter
        vm.loadSchoolDetail(schoolId)
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.cvBookmark.setOnClickListener {
            binding.ivBookmark.isSelected = !binding.ivBookmark.isSelected
        }
    }

    private fun updateUi(uiState: SchoolDetailUiState) {
        when (uiState) {
            is SchoolDetailUiState.Loading,
            is SchoolDetailUiState.Error,
            -> Unit

            is SchoolDetailUiState.Success -> handleSuccess(uiState)
        }
    }

    private fun handleSuccess(uiState: SchoolDetailUiState.Success) {
        binding.successUiState = uiState
        binding.ivSchoolBackground.setColorFilter(Color.parseColor("#66000000"))
        adapter.submitList(uiState.festivals)
        binding.llcSchoolSocialMedia.removeAllViews()
        uiState.schoolInfo.socialMedia.forEach { sm ->
            binding.llcSchoolSocialMedia.addView(
                SocialMediaView(requireActivity(), null, sm.logoUrl, sm.url)
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val SCHOOL_ID_KEY = "SCHOOL_ID_KEY"

        fun newInstance(id: Long) = SchoolDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(SCHOOL_ID_KEY, id)
            }
        }
    }
}
