package com.festago.festago.presentation.ui.notificationlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.presentation.databinding.ActivityNotificationListBinding

class NotificationListActivity : AppCompatActivity() {

    private val binding: ActivityNotificationListBinding by lazy {
        ActivityNotificationListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
