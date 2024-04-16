package com.festago.festago.presentation.ui.splash

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.festago.festago.presentation.BuildConfig
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ActivitySplashBinding
import com.festago.festago.presentation.ui.home.HomeActivity
import com.festago.festago.presentation.ui.signin.SignInActivity
import com.festago.festago.presentation.util.repeatOnStarted
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
    private val vm: SplashViewModel by viewModels()

    init {
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashScreen()
        initKakaoSdk()
        initObserve()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        checkAppUpdate()
    }

    private fun initSplashScreen() {
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(this.applicationContext, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    private fun initObserve() {
        repeatOnStarted(this) {
            vm.event.collect {
                when (it) {
                    is SplashEvent.ShowHome -> navigateToHome()
                    is SplashEvent.ShowSignIn -> navigateToSignIn()
                }
            }
        }
    }

    private fun checkAppUpdate() {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val currentVersion = try {
                    packageManager.getPackageInfo(packageName, 0).longVersionCode
                } catch (e: Exception) {
                    e.printStackTrace()
                    handleError()
                    return@addOnCompleteListener
                }
                val latestVersion = firebaseRemoteConfig.getLong(KEY_LATEST_VERSION)
                if (latestVersion == currentVersion) {
                    vm.checkSignIn()
                    return@addOnCompleteListener
                }
                splashScreen.setKeepOnScreenCondition { false }
                requestUpdate(latestVersion)
            }
        }
    }

    private fun requestUpdate(latestVersion: Long) {
        val isForceUpdateVersion = firebaseRemoteConfig.getBoolean(KEY_FORCE_UPDATE_REQUIRED)
        val latestVersionDescription =
            firebaseRemoteConfig.getString(KEY_LATEST_VERSION_DESCRIPTION)
        if (isForceUpdateVersion) {
            requestForceUpdate(message = latestVersionDescription)
            return
        }
        requestOptionalUpdate(latestVersion = latestVersion, message = latestVersionDescription)
    }

    private fun requestForceUpdate(message: String) {
        alertUpdate(message = message, update = ::handleUpdate, cancel = ::handleCancelForceUpdate)
    }

    private fun requestOptionalUpdate(latestVersion: Long, message: String) {
        val sharedPref = getPreferences(MODE_PRIVATE) ?: return
        val storedLatestVersion = sharedPref.getLong(KEY_STORED_LATEST_VERSION, 0L)
        if (latestVersion == storedLatestVersion) {
            vm.checkSignIn()
            return
        }
        alertUpdate(message = message, update = ::handleUpdate) {
            handleOptionalUpdateCancel(sharedPref, latestVersion)
        }
    }

    private fun handleOptionalUpdateCancel(sharedPref: SharedPreferences, latestVersion: Long) {
        updateStoredLatestVersion(sharedPref, latestVersion)
        vm.checkSignIn()
    }

    private fun updateStoredLatestVersion(sharedPref: SharedPreferences, latestVersion: Long) {
        with(sharedPref.edit()) {
            putLong(KEY_STORED_LATEST_VERSION, latestVersion)
            apply()
        }
    }

    private fun handleUpdate() {
        navigateToAppStore()
        finish()
    }

    private fun navigateToHome() {
        startActivity(HomeActivity.getIntent(this))
        finish()
    }

    private fun navigateToSignIn() {
        startActivity(SignInActivity.getIntent(this))
        finish()
    }

    private fun navigateToAppStore() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        finish()
    }

    private fun handleError() {
        Toast.makeText(
            this@SplashActivity,
            getString(R.string.splash_app_default_error_message),
            Toast.LENGTH_SHORT,
        ).show()
        finish()
    }

    private fun alertUpdate(message: String, update: () -> Unit, cancel: () -> Unit) {
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
        private const val KEY_STORED_LATEST_VERSION = "KEY_STORED_LATEST_VERSION"
        private const val DEBUG_REMOTE_CONFIG_FETCH_INTERVAL = 0L
        private const val RELEASE_REMOTE_CONFIG_FETCH_INTERVAL = 3600L
        private const val KEY_FORCE_UPDATE_REQUIRED = "FORCE_UPDATE_REQUIRED"
        private const val KEY_LATEST_VERSION = "LATEST_VERSION"
        private const val KEY_LATEST_VERSION_DESCRIPTION = "LATEST_VERSION_DESCRIPTION"
    }
}
