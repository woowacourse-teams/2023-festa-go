package com.festago.festago.presentation.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ActivityHomeBinding
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailFragment
import com.festago.festago.presentation.util.setOnApplyWindowInsetsCompatListener
import com.festago.festago.presentation.util.setStatusBarMode
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val vm: HomeViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        navController =
            (supportFragmentManager.findFragmentById(R.id.fcvHomeContainer) as NavHostFragment).navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nvHome)
        bottomNavigationView.setupWithNavController(navController)

        initBackPressedDispatcher()
        initBackStackListener()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.root.setOnApplyWindowInsetsCompatListener { view, windowInsets ->
            val navigationInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.setPadding(0, 0, 0, navigationInsets.bottom)
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
            windowInsets
        }

        binding.nvHome.setOnApplyWindowInsetsCompatListener { _, windowInsets ->
            windowInsets
        }
    }

    private fun initBinding() {
        setContentView(binding.root)
    }

    private fun initBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fcvHomeContainer)
            if (fragment is ArtistDetailFragment) {
                setStatusBarMode(isLight = false, backgroundColor = Color.TRANSPARENT)
            } else {
                setStatusBarMode(isLight = true, backgroundColor = Color.TRANSPARENT)
            }
        }
    }

    private fun initBackPressedDispatcher() {
        var backPressedTime = START_BACK_PRESSED_TIME
        onBackPressedDispatcher.addCallback {
            if ((System.currentTimeMillis() - backPressedTime) > FINISH_BACK_PRESSED_TIME) {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(
                    this@HomeActivity,
                    getString(R.string.home_back_pressed),
                    Toast.LENGTH_SHORT,
                ).show()
            } else {
                finish()
            }
        }
    }

    companion object {
        private const val START_BACK_PRESSED_TIME = 0L
        private const val FINISH_BACK_PRESSED_TIME = 3000L

        fun getIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
}
