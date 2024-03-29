package com.festago.festago.presentation.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.festago.festago.presentation.BuildConfig
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ActivitySplashBinding
import com.festago.festago.presentation.ui.home.HomeActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
        initKakaoSdk()
        checkAppUpdate(splashScreen)
        setContentView(binding.root)
    }

    private fun initKakaoSdk() {
        Log.d("SplashActivity", "initKakaoSdk: ${BuildConfig.KAKAO_NATIVE_APP_KEY}")
        KakaoSdk.init(this.applicationContext, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    private fun checkAppUpdate(splashScreen: SplashScreen) {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                handleOnSuccess(appUpdateInfo, splashScreen)
            }.addOnFailureListener {
                showHome()
            }
    }

    private fun handleOnSuccess(appUpdateInfo: AppUpdateInfo, splashScreen: SplashScreen) {
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            splashScreen.setKeepOnScreenCondition { false }
            requestUpdate()
        } else {
            showHome()
        }
    }

    private fun showHome() {
        startActivity(HomeActivity.getIntent(this))
        finish()
    }

    private fun requestUpdate() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.splash_app_update_request_dialog_title))
            setMessage(getString(R.string.splash_app_update_request_dialog_message))
            setNegativeButton(R.string.ok_dialog_btn_cancel) { _, _ ->
                handleCancelUpdate()
            }
            setPositiveButton(R.string.ok_dialog_btn_ok) { _, _ ->
                handleOkUpdate()
            }
            setCancelable(false)
        }.show()
    }

    private fun handleCancelUpdate() {
        Toast.makeText(
            this@SplashActivity,
            getString(R.string.splash_app_update_denied),
            Toast.LENGTH_SHORT,
        ).show()
        finish()
    }

    private fun handleOkUpdate() {
        navigateToAppStore()
        finish()
    }

    private fun navigateToAppStore() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        finish()
    }
}
