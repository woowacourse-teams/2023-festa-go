package com.festago.festago.presentation.ui.home.mypage

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.R
import com.festago.festago.databinding.FragmentMyPageBinding
import com.festago.festago.presentation.ui.home.HomeActivity
import com.festago.festago.presentation.ui.selectschool.SelectSchoolActivity
import com.festago.festago.presentation.ui.signin.SignInActivity
import com.festago.festago.presentation.ui.tickethistory.TicketHistoryActivity
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment(R.layout.fragment_my_page) {

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val vm: MyPageViewModel by viewModels()

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
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiState.collect { uiState ->
                handleUiState(uiState)
            }
        }
        repeatOnStarted(viewLifecycleOwner) {
            vm.event.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleUiState(uiState: MyPageUiState) {
        binding.uiState = uiState
        when (uiState) {
            is MyPageUiState.Loading, is MyPageUiState.Error -> Unit

            is MyPageUiState.Success -> handleSuccess(uiState)
        }
    }

    private fun handleEvent(event: MyPageEvent) {
        when (event) {
            is MyPageEvent.ShowSignIn -> handleShowSignInEvent()
            is MyPageEvent.SignOutSuccess -> handleSignOutSuccessEvent()
            is MyPageEvent.DeleteAccountSuccess -> handleDeleteAccountSuccess()
            is MyPageEvent.ShowTicketHistory -> handleShowTicketHistory()
            is MyPageEvent.ShowConfirmDelete -> handleShowConfirmDelete()
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
            binding.srlMyPage.isRefreshing = false
        }

        binding.tvSchoolAuthorization.setOnClickListener {
            startActivity(SelectSchoolActivity.getIntent(requireContext()))
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
