package com.festago.festago.presentation.ui.splash

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.festago.festago.presentation.BuildConfig
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ActivitySplashBinding
import com.festago.festago.presentation.ui.home.HomeActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private val firebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }
    private lateinit var splashScreen: SplashScreen
    private val configSettings by lazy {
        FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(if (BuildConfig.DEBUG) DEBUG_REMOTE_CONFIG_FETCH_INTERVAL else RELEASE_REMOTE_CONFIG_FETCH_INTERVAL)
            .build()
    }

    init {
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
        initKakaoSdk()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        checkAppUpdate()
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(this.applicationContext, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    private fun checkAppUpdate() {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val isCurrentVersion = firebaseRemoteConfig.getBoolean(KEY_IS_CURRENT_VERSION)
                if (isCurrentVersion) {
                    navigateToHome()
                    return@addOnCompleteListener
                }
                splashScreen.setKeepOnScreenCondition { false }
                requestUpdate()
            }
        }
    }

    private fun requestUpdate() {
        val forceUpdate = firebaseRemoteConfig.getBoolean(KEY_FORCE_UPDATE_REQUIRED)
        val currentVersionDescription =
            firebaseRemoteConfig.getString(KEY_CURRENT_VERSION_DESCRIPTION)
        if (forceUpdate) {
            requestForceUpdate(message = currentVersionDescription)
            return
        }
        requestOptionalUpdate(message = currentVersionDescription)
    }

    private fun requestOptionalUpdate(message: String) {
        alertUpdate(update = ::handleUpdate, cancel = ::navigateToHome, message)
    }

    private fun requestForceUpdate(message: String) {
        alertUpdate(update = ::handleUpdate, cancel = ::handleCancelForceUpdate, message)
    }

    private fun handleUpdate() {
        navigateToAppStore()
        finish()
    }

    private fun navigateToHome() {
        startActivity(HomeActivity.getIntent(this))
        finish()
    }

    private fun navigateToAppStore() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        finish()
    }

    private fun alertUpdate(update: () -> Unit, cancel: () -> Unit, message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.splash_app_update_request_dialog_title))
            setMessage(message)
            setPositiveButton(R.string.ok_dialog_btn_update) { _, _ -> update() }
            setNegativeButton(R.string.ok_dialog_btn_cancel) { _, _ -> cancel() }
            setCancelable(false)
        }.show()
    }

    private fun handleCancelForceUpdate() {
        Toast.makeText(
            this@SplashActivity,
            getString(R.string.splash_app_update_denied),
            Toast.LENGTH_SHORT,
        ).show()
        finish()
    }

    companion object {
        private const val DEBUG_REMOTE_CONFIG_FETCH_INTERVAL = 0L
        private const val RELEASE_REMOTE_CONFIG_FETCH_INTERVAL = 3600L
        private const val KEY_FORCE_UPDATE_REQUIRED = "FORCE_UPDATE_REQUIRED"
        private const val KEY_IS_CURRENT_VERSION = "IS_CURRENT_VERSION"
        private const val KEY_CURRENT_VERSION_DESCRIPTION = "CURRENT_VERSION_DESCRIPTION"
    }
}
