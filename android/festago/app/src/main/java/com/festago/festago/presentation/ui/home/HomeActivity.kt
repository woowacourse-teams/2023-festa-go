package com.festago.festago.presentation.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val vm: HomeViewModel by viewModels()

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        initObserve()
        initActivityResult()
    }

    private fun initBinding() {
        setContentView(binding.root)
    }

    private fun initView() {
        binding.bnvHome.setOnItemSelectedListener {
            vm.selectItem(getItemType(it.itemId))
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
        binding.fabTicket.isSelected = false
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

    private fun initActivityResult() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == SignInActivity.RESULT_NOT_SIGN_IN) {
                    binding.bnvHome.selectedItemId = R.id.item_festival
                }
            }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
}
