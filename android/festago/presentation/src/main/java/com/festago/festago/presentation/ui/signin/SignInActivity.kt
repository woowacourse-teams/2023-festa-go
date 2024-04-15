package com.festago.festago.presentation.ui.signin

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.festago.festago.presentation.databinding.ActivitySignInBinding
import com.festago.festago.presentation.ui.home.HomeActivity
import com.festago.festago.presentation.util.setOnApplyWindowInsetsCompatListener
import com.festago.festago.presentation.util.setStatusBarMode
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {

    private val binding: ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(binding.root)
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

    private fun initView() {
        initWindowInsets()
        initKakaoLogin()
        initWithoutLogin()
    }

    private fun initKakaoLogin() {
        binding.btnKakaoLogin.setOnClickListener {
            lifecycleScope.launch {
                KakaoAuthorization().getAuthCode(this@SignInActivity)
                    .onSuccess {
                        startActivity(HomeActivity.getIntent(this@SignInActivity))
                        finish()
                    }.onFailure {}
            }
        }
    }

    private fun initWithoutLogin() {
        binding.tvWithoutLogin.setOnClickListener {
            startActivity(HomeActivity.getIntent(this))
            finish()
        }
    }

    companion object {

        fun getIntent(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }
}
