package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.Stage
import com.festago.festago.presentation.model.StageUiModel

fun Stage.toPresentation() = StageUiModel(
    id = id,
    name = name,
    startTime = startTime,
)

fun StageUiModel.toDomain() = Stage(
    id = id,
    name = name,
    startTime = startTime,
)
