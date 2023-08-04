package com.festago.festago.presentation.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.festago.festago.R
import com.festago.festago.databinding.ActivityHomeBinding
import com.festago.festago.presentation.ui.home.festivallist.FestivalListFragment
import com.festago.festago.presentation.ui.home.mypage.MyPageFragment
import com.festago.festago.presentation.ui.home.ticketlist.TicketListFragment

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initView()
    }

    private fun initBinding() {
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initView() {
        binding.bnvHome.setOnItemSelectedListener {
            when (getItemType(it.itemId)) {
                HomeItemType.FESTIVAL_LIST -> {
                    changeFragment<FestivalListFragment>()
                    binding.fabTicket.isSelected = false
                }

                HomeItemType.TICKET_LIST -> {
                    changeFragment<TicketListFragment>()
                    binding.fabTicket.isSelected = true
                }

                HomeItemType.MY_PAGE -> {
                    changeFragment<MyPageFragment>()
                    binding.fabTicket.isSelected = false
                }
            }
            true
        }

        binding.fabTicket.setOnClickListener {
            binding.bnvHome.selectedItemId = R.id.item_ticket
        }

        changeFragment<FestivalListFragment>()
    }

    private fun getItemType(menuItemId: Int): HomeItemType {
        return when (menuItemId) {
            R.id.item_festival -> HomeItemType.FESTIVAL_LIST
            R.id.item_mypage -> HomeItemType.MY_PAGE
            R.id.item_ticket -> HomeItemType.TICKET_LIST
            else -> throw IllegalArgumentException("menu item id not found")
        }
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
}
