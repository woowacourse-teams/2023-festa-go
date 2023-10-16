package com.festago.festago.presentation.util

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

fun Activity.requestNotificationPermission(resultLauncher: ActivityResultLauncher<String>) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        checkSelfPermission(
            this,
            POST_NOTIFICATIONS,
            onNotGranted = {
                resultLauncher.launch(POST_NOTIFICATIONS)
            }
        )
    }
}

fun checkNotificationPermission(context: Context, block: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        checkSelfPermission(context, POST_NOTIFICATIONS, onGranted = block)
    } else {
        block()
    }
}

fun checkSelfPermission(
    context: Context,
    permission: String,
    onGranted: () -> Unit = {},
    onNotGranted: () -> Unit = {}
) {
    if (ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED) {
        onGranted()
    } else {
        onNotGranted()
    }
}
