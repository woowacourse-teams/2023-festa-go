package com.festago.festago.data.util

import com.festago.festago.domain.model.token.Token

fun Token.format() = "Bearer $token"
