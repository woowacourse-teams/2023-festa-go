package com.festago.festago.data.service

import com.festago.festago.data.dto.user.RefreshResponse
import com.festago.festago.data.dto.user.SignInResponse
import com.festago.festago.data.dto.user.SignOutResponse
import retrofit2.Response

interface UserRetrofitService {
    suspend fun signIn(code: String): Response<SignInResponse>
    suspend fun signOut(): Response<SignOutResponse>
    suspend fun refresh(refreshToken: String): Response<RefreshResponse>
    suspend fun deleteAccount(): Response<Unit>
}
