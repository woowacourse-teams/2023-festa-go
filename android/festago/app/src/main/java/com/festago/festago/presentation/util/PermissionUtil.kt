package com.festago.festago.presentation.util

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

fun Activity.requestNotificationPermission(resultLauncher: ActivityResultLauncher<String>) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
            resultLauncher.launch(POST_NOTIFICATIONS)
        }
    }
}
