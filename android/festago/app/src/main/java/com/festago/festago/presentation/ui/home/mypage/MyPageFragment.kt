package com.festago.festago.presentation.ui.home.mypage

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.R
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.data.datasource.AuthLocalDataSource
import com.festago.festago.data.repository.AuthDefaultRepository
import com.festago.festago.data.repository.TicketDefaultRepository
import com.festago.festago.data.repository.UserDefaultRepository
import com.festago.festago.data.retrofit.AuthRetrofitClient
import com.festago.festago.data.retrofit.NormalRetrofitClient
import com.festago.festago.databinding.FragmentMyPageBinding
import com.festago.festago.presentation.ui.home.HomeActivity
import com.festago.festago.presentation.ui.home.mypage.MyPageViewModel.MyPageViewModelFactory
import com.festago.festago.presentation.ui.signin.SignInActivity
import com.festago.festago.presentation.ui.tickethistory.TicketHistoryActivity

class MyPageFragment : Fragment(R.layout.fragment_my_page) {

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val vm: MyPageViewModel by viewModels {
        MyPageViewModelFactory(
            userRepository = UserDefaultRepository(
                userProfileService = AuthRetrofitClient.instance.userRetrofitService,
            ),
            ticketRepository = TicketDefaultRepository(
                ticketRetrofitService = AuthRetrofitClient.instance.ticketRetrofitService,
            ),
            authRepository = AuthDefaultRepository(
                authRetrofitService = NormalRetrofitClient.authRetrofitService,
                authDataSource = AuthLocalDataSource.getInstance(requireContext()),
                userRetrofitService = AuthRetrofitClient.instance.userRetrofitService,
            ),
            analyticsHelper = FirebaseAnalyticsHelper,
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
                is MyPageUiState.Loading, is MyPageUiState.Error -> Unit

                is MyPageUiState.Success -> handleSuccess(uiState)
            }
            binding.srlMyPage.isRefreshing = false
        }
        vm.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is MyPageEvent.ShowSignIn -> handleShowSignInEvent()
                is MyPageEvent.SignOutSuccess -> handleSignOutSuccessEvent()
                is MyPageEvent.DeleteAccountSuccess -> handleDeleteAccountSuccess()
                is MyPageEvent.ShowTicketHistory -> handleShowTicketHistory()
                is MyPageEvent.ShowConfirmDelete -> handleShowConfirmDelete()
            }
        }
    }

    private fun handleShowSignInEvent() {
        startActivity(SignInActivity.getIntent(requireContext()))
    }

    private fun handleSignOutSuccessEvent() {
        restartHome()
    }

    private fun handleDeleteAccountSuccess() {
        restartHome()
    }

    private fun restartHome() {
        requireActivity().finishAffinity()
        startActivity(HomeActivity.getIntent(requireContext()))
    }

    private fun handleShowTicketHistory() {
        startActivity(TicketHistoryActivity.getIntent(requireContext()))
    }

    private fun handleShowConfirmDelete() {
        val dialog = AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.confirm_delete_dialog_title))
            setMessage(getString(R.string.confirm_delete_dialog_message))
            setPositiveButton(getString(R.string.confirm_delete_dialog_yes)) { dialog, _ ->
                vm.deleteAccount()
                dialog.dismiss()
            }
            setNegativeButton(getString(R.string.confirm_delete_dialog_no)) { dialog, _ ->
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun initView() {
        binding.vm = vm

        vm.loadUserInfo()

        binding.srlMyPage.setOnRefreshListener {
            vm.loadUserInfo()
        }
    }

    private fun handleSuccess(uiState: MyPageUiState.Success) {
        binding.successState = uiState
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
