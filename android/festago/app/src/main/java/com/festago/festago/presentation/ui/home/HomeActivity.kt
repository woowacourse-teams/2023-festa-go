package com.festago.festago.presentation.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import com.festago.festago.presentation.util.requestNotificationPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        initObserve()
        initNotificationPermission()
    }

    private fun initBinding() {
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initView() {
        binding.bnvHome.setOnItemSelectedListener {
            vm.loadHomeItem(getItemType(it.itemId))
            true
        }

        binding.fabTicket.setOnClickListener {
            binding.bnvHome.selectedItemId = R.id.item_ticket
        }

        changeFragment<FestivalListFragment>()
    }

    private fun initObserve() {
        repeatOnStarted(this) {
            vm.event.collect { event ->
                when (event) {
                    is HomeEvent.ShowFestivalList -> showFestivalList()
                    is HomeEvent.ShowTicketList -> showTicketList()
                    is HomeEvent.ShowMyPage -> showMyPage()
                    is HomeEvent.ShowSignIn -> showSignIn()
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
        startActivity(SignInActivity.getIntent(this))
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
