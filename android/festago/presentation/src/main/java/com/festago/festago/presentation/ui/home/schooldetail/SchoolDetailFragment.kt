package com.festago.festago.presentation.ui.home.schooldetail

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentSchoolDetailBinding
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

        initObserve()
        initView()
    }

    private fun initObserve() {
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiState.collect {
                binding.uiState = it
                updateUi(it)
            }
        }
    }

    private fun initView() {
        adapter = SchoolFestivalListAdapter()
        binding.rvFestivalList.adapter = adapter
        vm.loadSchoolDetail()
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
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
