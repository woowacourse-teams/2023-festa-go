package com.festago.festago.data.datasource

import com.festago.festago.data.model.TokenEntity

interface TokenDataSource {
    var accessToken: TokenEntity?
    var refreshToken: TokenEntity?
}
