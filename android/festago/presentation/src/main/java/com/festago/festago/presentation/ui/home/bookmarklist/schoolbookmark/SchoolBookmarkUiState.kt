package com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark

data class SchoolBookmarkUiState(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val onSchoolDetail: (school: SchoolBookmarkUiState) -> Unit,
)
