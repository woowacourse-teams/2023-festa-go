package com.festago.festago.domain.repository

import com.festago.festago.domain.model.Token

interface AuthRepository {
    val isSigned: Boolean
    fun findToken(): String?
    suspend fun signIn(socialType: String, token: String): Result<Token>
    fun storeToken(token: String)
}
