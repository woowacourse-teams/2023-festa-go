package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.Festival
import com.festago.festago.presentation.model.FestivalUiModel

fun Festival.toPresentation(): FestivalUiModel =
    FestivalUiModel(id, name, startDate, endDate, thumbnail)
