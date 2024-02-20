package com.festago.festago.domain.model.artist

import com.festago.festago.domain.model.festival.Festival

data class Stages(
    val last: Boolean,
    val stage: List<Festival>,
)
