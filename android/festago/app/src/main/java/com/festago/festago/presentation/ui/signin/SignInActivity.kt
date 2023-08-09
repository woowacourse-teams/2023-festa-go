package com.festago.festago.presentation.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.festago.festago.R
import com.festago.festago.data.RetrofitClient
import com.festago.festago.data.datasource.SharedPrefAuthDataSource
import com.festago.festago.data.repository.AuthDefaultRepository
import com.festago.festago.databinding.ActivitySignInBinding
import com.festago.festago.presentation.ui.customview.OkDialogFragment
import com.festago.festago.presentation.ui.home.HomeActivity
import com.festago.festago.presentation.ui.signin.SignInViewModel.SignInViewModelFactory
import com.festago.festago.presentation.util.loginWithKakao
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    private val vm: SignInViewModel by viewModels {
        SignInViewModelFactory(
            AuthDefaultRepository(
                RetrofitClient.instance.authRetrofitService,
                SharedPrefAuthDataSource.getInstance(this),
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserve()
        initComment()
    }

    private fun initView() {
        binding = ActivitySignInBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.vm = vm
        setContentView(binding.root)
        initLogout()
    }

    private fun initObserve() {
        vm.event.observe(this) { event ->
            when (event) {
                SignInEvent.ShowSignInPage -> signIn()
                SignInEvent.SignInSuccess -> handleSuccessEvent()
                SignInEvent.SignInFailure -> handleFailureEvent()
            }
        }
    }

    private fun handleSuccessEvent() {
        finishAffinity()
        startMain()
    }

    private fun handleFailureEvent() {
        val dialog = OkDialogFragment.newInstance("예약에 실패하였습니다.").apply {
            listener = OkDialogFragment.OnClickListener {
                finishAffinity()
                startMain()
            }
        }
        dialog.show(supportFragmentManager, OkDialogFragment::class.java.name)
    }

    private fun startMain() {
        val intent = HomeActivity.getIntent(this).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intent)
    }

    private fun signIn() {
        lifecycleScope.launch {
            val oauthToken = UserApiClient.loginWithKakao(this@SignInActivity)
            vm.signIn(oauthToken.accessToken)
        }
    }

    private fun initLogout() {
        // todo:logout 처리 필요
        binding.ivFestagoLogo.setOnClickListener {
            UserApiClient.instance.unlink { error -> Log.d("choiseonghoon", error.toString()) }
        }
    }

    private fun initComment() {
        val spannableStringBuilder = SpannableStringBuilder(
            getString(R.string.mypage_tv_signin_description),
        ).apply {
            setSpan(
                ForegroundColorSpan(getColor(R.color.seed)),
                COLOR_SPAN_START_INDEX,
                COLOR_SPAN_END_INDEX,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
        binding.tvLoginDescription.text = spannableStringBuilder
    }

    companion object {
        private const val COLOR_SPAN_START_INDEX = 0
        private const val COLOR_SPAN_END_INDEX = 4

        fun getIntent(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }
}
