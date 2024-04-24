package com.festago.festago.data.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.festago.festago.data.model.TokenEntity
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenLocalDataSource @Inject constructor(
    @ApplicationContext context: Context,
) : TokenDataSource {

    private val sharedPreference: SharedPreferences by lazy {
        val masterKeyAlias = MasterKey
            .Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences(context, ENCRYPTED_PREF_FILE, masterKeyAlias)
    }

    override var accessToken: TokenEntity?
        get() = sharedPreference.getToken(ACCESS_TOKEN_KEY, null)
        set(value) {
            sharedPreference.putToken(ACCESS_TOKEN_KEY, value)
        }

    override var refreshToken: TokenEntity?
        get() = sharedPreference.getToken(REFRESH_TOKEN_KEY, null)
        set(value) {
            sharedPreference.putToken(REFRESH_TOKEN_KEY, value)
        }

    private fun SharedPreferences.putToken(key: String, token: TokenEntity?) {
        val jsonString = GsonBuilder().create().toJson(token)
        edit().putString(key, jsonString).apply()
    }

    private fun SharedPreferences.getToken(key: String, default: TokenEntity?): TokenEntity? {
        val token = getString(key, null) ?: return default
        return GsonBuilder().create().fromJson(token, TokenEntity::class.java)
    }

    companion object {
        private const val ENCRYPTED_PREF_FILE = "encrypted_pref_file"
        private const val ACCESS_TOKEN_KEY = "access_token_key"
        private const val REFRESH_TOKEN_KEY = "refresh_token_key"
    }
}
