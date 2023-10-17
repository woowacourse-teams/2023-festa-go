package com.festago.festago.presentation.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.festago.festago.R
import com.festago.festago.databinding.ActivityHomeBinding
import com.festago.festago.presentation.ui.home.festivallist.FestivalListFragment
import com.festago.festago.presentation.ui.home.mypage.MyPageFragment
import com.festago.festago.presentation.ui.home.ticketlist.TicketListFragment
import com.festago.festago.presentation.ui.signin.SignInActivity
import com.festago.festago.presentation.util.repeatOnStarted
import com.google.android.material.navigation.NavigationBarView
import com.festago.festago.presentation.util.requestNotificationPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val vm: HomeViewModel by viewModels()

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val navigationBarView by lazy { binding.nvHome as NavigationBarView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        initObserve()
        initResultLauncher()
    }

    private fun initResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == SignInActivity.RESULT_NOT_SIGN_IN) {
                    navigationBarView.selectedItemId = R.id.item_festival
                }
            }
        initNotificationPermission()
    }

    private fun initBinding() {
        setContentView(binding.root)
    }

    private fun initView() {
        navigationBarView.setOnItemSelectedListener {
            vm.selectItem(getItemType(it.itemId))
            true
        }

        binding.fabTicket.setOnClickListener {
            navigationBarView.selectedItemId = R.id.item_ticket
        }

        changeFragment<FestivalListFragment>()
    }

    private fun initObserve() {
        repeatOnStarted(this) {
            vm.event.collect { event ->
                when (event) {
                    is HomeEvent.ShowSignIn -> showSignIn()
                }
            }
        }

        repeatOnStarted(this) {
            vm.selectedItem.collect { homeItemType ->
                when (homeItemType) {
                    HomeItemType.FESTIVAL_LIST -> showFestivalList()
                    HomeItemType.TICKET_LIST -> showTicketList()
                    HomeItemType.MY_PAGE -> showMyPage()
                }
            }
        }
    }

    private fun initNotificationPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(
                    this,
                    getString(R.string.home_notification_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        requestNotificationPermission(requestPermissionLauncher)
    }

    private fun getItemType(menuItemId: Int): HomeItemType {
        return when (menuItemId) {
            R.id.item_festival -> HomeItemType.FESTIVAL_LIST
            R.id.item_mypage -> HomeItemType.MY_PAGE
            R.id.item_ticket -> HomeItemType.TICKET_LIST
            else -> throw IllegalArgumentException("menu item id not found")
        }
    }

    private fun showFestivalList() {
        changeFragment<FestivalListFragment>()
        binding.fabTicket.isSelected = false
    }

    private fun showTicketList() {
        changeFragment<TicketListFragment>()
        binding.fabTicket.isSelected = true
    }

    private fun showMyPage() {
        changeFragment<MyPageFragment>()
        binding.fabTicket.isSelected = false
    }

    private fun showSignIn() {
        resultLauncher.launch(SignInActivity.getIntent(this))
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
            fragmentTransaction.add(R.id.fcv_home_container, targetFragment, tag)
        } else {
            fragmentTransaction.show(targetFragment)
        }

        fragmentTransaction.commit()
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
}
