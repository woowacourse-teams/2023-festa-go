package com.festago.festago.presentation.ui.home.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.R
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.data.RetrofitClient
import com.festago.festago.data.repository.TicketDefaultRepository
import com.festago.festago.data.repository.UserDefaultRepository
import com.festago.festago.databinding.FragmentMyPageBinding
import com.festago.festago.presentation.ui.home.mypage.MyPageViewModel.MyPageViewModelFactory

class MyPageFragment : Fragment(R.layout.fragment_my_page) {

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val vm: MyPageViewModel by viewModels {
        MyPageViewModelFactory(
            userRepository = UserDefaultRepository(
                userProfileService = RetrofitClient.getInstance().userRetrofitService,
            ),
            ticketRepository = TicketDefaultRepository(
                ticketRetrofitService = RetrofitClient.getInstance().ticketRetrofitService,
            ),
            analyticsHelper = FirebaseAnalyticsHelper.getInstance(),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserve()
        initView()
    }

    private fun initObserve() {
        vm.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.uiState = uiState
            when (uiState) {
                is MyPageUiState.Loading, is MyPageUiState.Error -> {
                    binding.srlMyPage.isRefreshing = false
                }

                is MyPageUiState.Success -> handleSuccess(uiState)
            }
        }
    }

    private fun initView() {
        vm.loadUserInfo()

        binding.srlMyPage.setOnRefreshListener {
            vm.loadUserInfo()
        }
    }

    private fun handleSuccess(uiState: MyPageUiState.Success) {
        binding.successState = uiState
        binding.srlMyPage.isRefreshing = false
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
