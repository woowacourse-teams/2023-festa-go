package com.festago.festago.presentation.ui.signin

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ActivitySignInBinding
import com.festago.festago.presentation.ui.home.HomeActivity
import com.festago.festago.presentation.util.repeatOnStarted
import com.festago.festago.presentation.util.setOnApplyWindowInsetsCompatListener
import com.festago.festago.presentation.util.setStatusBarMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private val binding: ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }

    private val vm: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserve()
        setContentView(binding.root)
    }

    private fun initView() {
        initWindowInsets()
        initKakaoLogin()
        initWithoutLogin()
    }

    private fun initObserve() {
        repeatOnStarted(this) {
            vm.event.collect {
                when (it) {
                    SignInEvent.ShowHome -> navigateToHome()
                    SignInEvent.SignInFailure -> handleSignInFailure()
                }
            }
        }
    }

    private fun initWindowInsets() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding.root.setOnApplyWindowInsetsCompatListener { view, windowInsets ->
            val navigationInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.setPadding(0, 0, 0, navigationInsets.bottom)
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
            setStatusBarMode(isLight = false, backgroundColor = Color.TRANSPARENT)
            windowInsets
        }
    }

    private fun initKakaoLogin() {
        binding.btnKakaoLogin.setOnClickListener {
            lifecycleScope.launch {
                KakaoAuthorization().getIdToken(this@SignInActivity)
                    .onSuccess { idToken ->
                        vm.signIn(idToken)
                    }.onFailure { error ->
                        handleSignInFailure()
                    }
            }
        }
    }

    private fun initWithoutLogin() {
        binding.tvWithoutLogin.setOnClickListener {
            vm.rejectSignIn()
        }
    }

    private fun navigateToHome() {
        startActivity(HomeActivity.getIntent(this))
        finish()
    }

    private fun handleSignInFailure() {
        Toast.makeText(
            this,
            getString(R.string.sign_in_default_error_message),
            Toast.LENGTH_SHORT,
        ).show()
    }

    companion object {

        fun getIntent(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }
}
