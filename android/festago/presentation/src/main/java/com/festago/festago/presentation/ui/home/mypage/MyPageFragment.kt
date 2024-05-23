package com.festago.festago.presentation.ui.home.mypage

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.FragmentMyPageBinding
import com.festago.festago.presentation.ui.signin.SignInActivity
import com.festago.festago.presentation.util.repeatOnStarted
import com.festago.festago.presentation.util.setOnApplyWindowInsetsCompatListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val vm: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater)
        binding.root.setOnApplyWindowInsetsCompatListener { view, windowInsets ->
            val statusBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBarInsets.top, 0, 0)
            windowInsets
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
    }

    private fun initView() {
        vm.loadUserInfo()
        initSignInBtn()
        initPersonalInfoBtn()
        initAppVersionBtn()
        initLogoutBtn()
        initAskQuestionBtn()
        initDeleteAccountBtn()
    }

    private fun initPersonalInfoBtn() {
        binding.tvPersonalInfoPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://sites.google.com/view/privacy-festago")
            startActivity(intent)
        }
    }

    private fun initAskQuestionBtn() {
        binding.tvAskQuestion.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://forms.gle/UWQe28gEUFy3AWPB8")
            startActivity(intent)
        }
    }

    private fun initSignInBtn() {
        binding.llMyPageLogin.setOnClickListener {
            vm.signIn()
        }
    }

    private fun initAppVersionBtn() {
        binding.tvAppVersion.setOnClickListener {
            showAppVersion()
        }
    }

    private fun initLogoutBtn() {
        binding.tvLogout.setOnClickListener {
            alertDialog(
                getString(R.string.my_page_logout_alert_title),
                getString(R.string.my_page_logout_alert_message),
                action = { vm.signOut() },
                cancel = { },
            )
        }
    }

    private fun initDeleteAccountBtn() {
        binding.tvDeleteAccount.setOnClickListener {
            alertDialog(
                getString(R.string.my_page_delete_account_alert_title),
                getString(R.string.my_page_delete_account_alert_message),
                action = { vm.deleteAccount() },
                cancel = { },
            )
        }
    }

    private fun showAppVersion() {
        val appVersion = requireActivity()
            .packageManager
            .getPackageInfo(requireActivity().packageName, 0)
            .versionName

        Toast.makeText(
            requireContext(),
            getString(R.string.my_page_app_version_format, appVersion),
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun initObserve() {
        repeatOnStarted(viewLifecycleOwner) {
            vm.uiState.collect { uiState ->
                binding.uiState = uiState
                when (uiState) {
                    is MyPageUiState.Loading,
                    is MyPageUiState.NotLoggedIn,
                    -> Unit

                    is MyPageUiState.Success -> {
                        binding.successUiState = uiState
                    }

                    is MyPageUiState.Error -> {
                        binding.errorUiState = uiState
                    }
                }
            }
        }

        repeatOnStarted(viewLifecycleOwner) {
            vm.event.collect { event ->
                when (event) {
                    MyPageEvent.ShowSignIn -> {
                        startActivity(SignInActivity.getIntent(requireContext()))
                    }
                }
            }
        }
    }

    private fun alertDialog(
        title: String,
        message: String,
        action: () -> Unit,
        cancel: () -> Unit,
    ) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(R.string.ok_dialog_btn_ok) { _, _ -> action() }
            setNegativeButton(R.string.ok_dialog_btn_cancel) { _, _ -> cancel() }
            setCancelable(false)
        }.show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
