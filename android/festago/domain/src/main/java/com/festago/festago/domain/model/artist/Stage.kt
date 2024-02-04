package com.festago.festago.domain.model.artist

data class Stage(
    val id: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val imageUrl: String,
    val artists: List<Artist>,
)