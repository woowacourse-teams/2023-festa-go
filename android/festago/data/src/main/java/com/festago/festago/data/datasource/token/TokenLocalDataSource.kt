package com.festago.festago.data.datasource.token

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.festago.festago.data.model.TokenEntity
import com.festago.festago.data.util.getObject
import com.festago.festago.data.util.putObject
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
        get() = sharedPreference.getObject<TokenEntity>(ACCESS_TOKEN_KEY, null)
        set(value) {
            sharedPreference.putObject<TokenEntity>(ACCESS_TOKEN_KEY, value)
        }

    override var refreshToken: TokenEntity?
        get() = sharedPreference.getObject<TokenEntity>(REFRESH_TOKEN_KEY, null)
        set(value) {
            sharedPreference.putObject<TokenEntity>(REFRESH_TOKEN_KEY, value)
        }

    companion object {
        private const val ENCRYPTED_PREF_FILE = "encrypted_pref_file"
        private const val ACCESS_TOKEN_KEY = "access_token_key"
        private const val REFRESH_TOKEN_KEY = "refresh_token_key"
    }
}
