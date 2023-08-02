package com.festago.festago.presentation.util

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= 33) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key)
    }
}

inline fun <reified T : Parcelable> Bundle.getParcelableArrayCompat(key: String): Array<T>? {
    return if (Build.VERSION.SDK_INT >= 33) {
        getParcelableArray(key, T::class.java)
    } else {
        getParcelableArray(key) as Array<T>?
    }
}
