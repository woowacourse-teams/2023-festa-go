package com.festago.festago.presentation.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ActivityHomeBinding
import com.festago.festago.presentation.ui.home.bookmarklist.BookmarkListFragment
import com.festago.festago.presentation.ui.home.festivallist.FestivalListFragment
import com.festago.festago.presentation.ui.home.mypage.MyPageFragment
import com.festago.festago.presentation.ui.home.ticketlist.TicketListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val vm: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        initBackPressedDispatcher()
    }

    private fun initBinding() {
        setContentView(binding.root)
    }

    private fun initView() {
        binding.nvHome.setOnItemSelectedListener {
            selectView(it.itemId)
            true
        }
        changeFragment<FestivalListFragment>()
    }

    private fun selectView(menuItemId: Int) {
        return when (menuItemId) {
            R.id.itemFestival -> showFestivalList()
            R.id.itemTicket -> showTicketList()
            R.id.itemBookmark -> showBookmarkList()
            R.id.itemMyPage -> showMyPage()
            else -> throw IllegalArgumentException("menu item id not found")
        }
    }

    private fun showFestivalList() {
        changeFragment<FestivalListFragment>()
    }

    private fun showTicketList() {
        changeFragment<TicketListFragment>()
    }

    private fun showBookmarkList() {
        changeFragment<BookmarkListFragment>()
    }

    private fun showMyPage() {
        changeFragment<MyPageFragment>()
    }

    private inline fun <reified T : Fragment> changeFragment() {
        val tag = T::class.java.name
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        supportFragmentManager.fragments.forEach { fragment ->
            fragmentTransaction.hide(fragment)
        }

        var targetFragment = supportFragmentManager.findFragmentByTag(tag)

        if (targetFragment == null) {
            targetFragment = T::class.java.newInstance()
            fragmentTransaction.add(R.id.fcvHomeContainer, targetFragment, tag)
        } else {
            fragmentTransaction.show(targetFragment)
        }

        fragmentTransaction.commit()
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
