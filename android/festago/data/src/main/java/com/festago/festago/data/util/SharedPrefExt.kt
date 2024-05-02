package com.festago.festago.data.util

import android.content.SharedPreferences
import com.google.gson.GsonBuilder

inline fun <reified T> SharedPreferences.putObject(key: String, value: T?) {
    val jsonString = GsonBuilder().create().toJson(value)
    edit().putString(key, jsonString).apply()
}

inline fun <reified T> SharedPreferences.getObject(key: String, default: T?): T? {
    val value = getString(key, null) ?: return default
    return GsonBuilder().create().fromJson(value, T::class.java)
}
