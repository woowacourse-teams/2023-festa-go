package com.festago.festago.di

import android.content.Context
import com.festago.festago.data.datasource.AuthDataSource
import com.festago.festago.data.datasource.AuthLocalDataSource

class LocalDataSourceContainer(context: Context) {
    val authDataSource: AuthDataSource = AuthLocalDataSource(context)
}
