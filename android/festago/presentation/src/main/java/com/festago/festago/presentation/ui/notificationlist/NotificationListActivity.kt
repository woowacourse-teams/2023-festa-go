package com.festago.festago.presentation.ui.notificationlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.presentation.databinding.ActivityNotificationListBinding

class NotificationListActivity : AppCompatActivity() {

    private val binding: ActivityNotificationListBinding by lazy {
        ActivityNotificationListBinding.inflate(layoutInflater)
    }

    private val vm: NotificationListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initBackPressed()
    }

    private fun initBackPressed() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, NotificationListActivity::class.java)
        }
    }
}
