package com.festago.festago.presentation.ui.search.screen

import com.festago.festago.presentation.ui.search.uistate.ArtistSearchItemUiState
import com.festago.festago.presentation.ui.search.uistate.FestivalSearchItemUiState
import com.festago.festago.presentation.ui.search.uistate.SchoolSearchItemUiState

sealed class ItemSearchScreenUiState(
    val screenPosition: Int,
) {
    data class FestivalSearchScreen(
        val festivalSearches: List<FestivalSearchItemUiState>,
        val onAddSearchQueryClick: () -> Unit = {},
    ) : ItemSearchScreenUiState(FESTIVAL_POSITION)

    data class ArtistSearchScreen(
        val artistSearches: List<ArtistSearchItemUiState>,
        val onAddSearchQueryClick: () -> Unit = {},
    ) : ItemSearchScreenUiState(ARTIST_POSITION)

    data class SchoolSearchScreen(
        val schoolSearches: List<SchoolSearchItemUiState>,
        val onAddSearchQueryClick: () -> Unit = {},
    ) : ItemSearchScreenUiState(SCHOOL_POSITION)

    companion object {
        const val FESTIVAL_POSITION = 0
        const val ARTIST_POSITION = 1
        const val SCHOOL_POSITION = 2
    }
}
