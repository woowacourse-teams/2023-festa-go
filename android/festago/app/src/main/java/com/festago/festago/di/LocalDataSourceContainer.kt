package com.festago.festago.di

import android.content.Context
import com.festago.festago.data.datasource.TokenDataSource
import com.festago.festago.data.datasource.TokenLocalDataSource

class LocalDataSourceContainer(context: Context) {
    val tokenDataSource: TokenDataSource = TokenLocalDataSource(context)
}
