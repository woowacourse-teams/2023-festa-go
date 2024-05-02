package com.festago.festago.data.datasource.userinfo

import android.content.Context
import android.content.SharedPreferences
import com.festago.festago.data.model.UserInfoEntity
import com.festago.festago.data.util.getObject
import com.festago.festago.data.util.putObject
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserInfoLocalDataSource @Inject constructor(
    @ApplicationContext context: Context,
) : UserInfoDataSource {

    private val sharedPreference: SharedPreferences by lazy {
        context.getSharedPreferences(USER_INFO_PREF, Context.MODE_PRIVATE)
    }

    override var userInfo: UserInfoEntity?
        get() = sharedPreference.getObject<UserInfoEntity>(USER_ID_KEY, null)
        set(value) {
            sharedPreference.putObject<UserInfoEntity>(USER_ID_KEY, value)
        }

    companion object {
        private const val USER_INFO_PREF = "user_info_pref"
        private const val USER_ID_KEY = "user_info_key"
    }
}
