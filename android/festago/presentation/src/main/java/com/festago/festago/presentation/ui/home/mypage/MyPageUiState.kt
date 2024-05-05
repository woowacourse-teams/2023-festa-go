package com.festago.festago.presentation.ui.home.mypage

import com.festago.festago.domain.model.user.UserInfo

sealed interface MyPageUiState {
    object Loading : MyPageUiState

    class Success(
        val userInfo: UserInfo,
    ) : MyPageUiState

    object NotLoggedIn : MyPageUiState

    class Error(
        val refresh: () -> Unit,
    ) : MyPageUiState

    val shouldShowNotLoggedIn get() = this is NotLoggedIn
    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
