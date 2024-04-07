package com.festago.festago.domain.model.search

data class ArtistSearch(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
    val todayStage: Int,
    val upcomingStage: Int,
)
