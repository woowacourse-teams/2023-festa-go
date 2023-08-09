package com.festago.festago.presentation.ui.home.mypage

import com.festago.festago.presentation.model.TicketUiModel
import com.festago.festago.presentation.model.UserProfileUiModel

sealed interface MyPageUiState {
    object Loading : MyPageUiState

    data class Success(
        val userProfile: UserProfileUiModel = UserProfileUiModel(),
        val ticket: TicketUiModel = TicketUiModel(),
    ) : MyPageUiState {
        val hasTicket: Boolean get() = ticket.id != -1L
    }

    object Error : MyPageUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
