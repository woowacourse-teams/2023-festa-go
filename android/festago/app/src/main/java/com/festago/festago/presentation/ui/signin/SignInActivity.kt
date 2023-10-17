package com.festago.festago.presentation.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.R
import com.festago.festago.databinding.ActivitySignInBinding
import com.festago.festago.presentation.ui.customview.OkDialogFragment
import com.festago.festago.presentation.ui.home.HomeActivity
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    private val vm: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        initObserve()
    }

    private fun initBinding() {
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initView() {
        binding.lifecycleOwner = this
        binding.vm = vm
        initComment()
        initBackPressed()
    }

    private fun initBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(RESULT_NOT_SIGN_IN, intent)
                finish()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initObserve() {
        repeatOnStarted(this) {
            vm.event.collect { event ->
                when (event) {
                    is SignInEvent.SignInSuccess -> handleSuccessEvent()
                    is SignInEvent.SignInFailure -> handleFailureEvent()
                }
            }
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

    private fun handleSuccessEvent() {
        showHomeWithFinish()
    }

    private fun handleFailureEvent() {
        val dialog = OkDialogFragment.newInstance(FAILURE_SIGN_IN).apply {
            listener = OkDialogFragment.OnClickListener {
                showHomeWithFinish()
            }
        }
        dialog.show(supportFragmentManager, OkDialogFragment::class.java.name)
    }

    private fun showHomeWithFinish() {
        val intent = HomeActivity.getIntent(this).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        finishAffinity()
        startActivity(intent)
    }

    companion object {
        private const val COLOR_SPAN_START_INDEX = 0
        private const val COLOR_SPAN_END_INDEX = 4

        private const val FAILURE_SIGN_IN = "로그인에 실패했습니다."
        const val RESULT_NOT_SIGN_IN = 1
        fun getIntent(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }
}
