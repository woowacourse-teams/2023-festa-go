package com.festago.festago.data.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SharedPrefAuthDataSource private constructor(
    context: Context,
) : AuthDataSource {

    private val sharedPreference: SharedPreferences by lazy {
        val masterKeyAlias = MasterKey
            .Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences(context, ENCRYPTED_PREF_FILE, masterKeyAlias)
    }

    override var token: String?
        get() = sharedPreference.getString(TOKEN_KEY, null)
        set(value) {
            sharedPreference.edit().putString(TOKEN_KEY, value).apply()
        }

    companion object {
        private const val ENCRYPTED_PREF_FILE = "encrypted_pref_file"
        private const val TOKEN_KEY = "TOKEN_KEY"

        private var INSTANCE: SharedPrefAuthDataSource? = null

        @Synchronized
        fun getInstance(context: Context): SharedPrefAuthDataSource {
            return INSTANCE ?: SharedPrefAuthDataSource(context).also { INSTANCE = it }
        }
    }
}
