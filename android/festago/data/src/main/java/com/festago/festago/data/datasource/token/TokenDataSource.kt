package com.festago.festago.data.datasource.token

import com.festago.festago.data.model.TokenEntity

interface TokenDataSource {
    var accessToken: TokenEntity?
    var refreshToken: TokenEntity?
}
