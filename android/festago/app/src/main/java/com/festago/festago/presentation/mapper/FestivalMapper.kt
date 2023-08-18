package com.festago.festago.presentation.mapper

import com.festago.festago.model.Festival
import com.festago.festago.presentation.model.FestivalUiModel

fun Festival.toPresentation(): FestivalUiModel =
    FestivalUiModel(id, name, startDate, endDate, thumbnail)

fun List<Festival>.toPresentation(): List<FestivalUiModel> = this.map { it.toPresentation() }
