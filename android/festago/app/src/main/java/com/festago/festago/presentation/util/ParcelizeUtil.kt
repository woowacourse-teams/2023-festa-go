package com.festago.festago.presentation.util

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= 33) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key)
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.getParcelableArrayListCompat(key: String): ArrayList<T>? {
    return if (Build.VERSION.SDK_INT >= 33) {
        getParcelableArrayList(key, T::class.java)
    } else {
        getParcelableArrayList(key)
    }
}

inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(key: String): T? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val bundle = extras ?: return null
        return BundleCompat.getParcelable(bundle, key, T::class.java)
    }
    @Suppress("DEPRECATION")
    return getParcelableExtra(key) as? T
}
