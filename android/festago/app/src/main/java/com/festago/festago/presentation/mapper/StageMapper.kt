package com.festago.festago.presentation.mapper

import com.festago.domain.model.Stage
import com.festago.festago.presentation.model.StageUiModel

fun Stage.toPresentation() = StageUiModel(
    id = id,
    startTime = startTime,
)

fun StageUiModel.toDomain() = Stage(
    id = id,
    startTime = startTime,
)
