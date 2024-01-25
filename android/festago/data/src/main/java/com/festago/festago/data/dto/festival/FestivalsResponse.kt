package com.festago.festago.data.dto.festival

data class FestivalsResponse(
    val festivals: List<FestivalResponse>,
    val isLastPage: Boolean,
)
