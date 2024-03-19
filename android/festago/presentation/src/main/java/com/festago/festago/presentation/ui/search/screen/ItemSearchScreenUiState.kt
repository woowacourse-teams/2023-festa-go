package com.festago.festago.presentation.ui.search.screen

import com.festago.festago.presentation.ui.search.uistate.ArtistSearchItemUiState
import com.festago.festago.presentation.ui.search.uistate.FestivalSearchItemUiState
import com.festago.festago.presentation.ui.search.uistate.SchoolSearchItemUiState

sealed class ItemSearchScreenUiState(val screenPosition: Int) {
    data class FestivalSearchScreen(val festivalSearches: List<FestivalSearchItemUiState>) :
        ItemSearchScreenUiState(0)

    data class ArtistSearchScreen(val artistSearches: List<ArtistSearchItemUiState>) :
        ItemSearchScreenUiState(1)

    data class SchoolSearchScreen(val schoolSearches: List<SchoolSearchItemUiState>) :
        ItemSearchScreenUiState(2)
}
