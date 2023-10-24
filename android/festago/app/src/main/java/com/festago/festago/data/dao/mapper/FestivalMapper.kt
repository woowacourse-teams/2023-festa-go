package com.festago.festago.data.dao.mapper

import com.festago.festago.data.dao.FestivalEntity
import com.festago.festago.model.Festival
import java.time.LocalDate

fun List<FestivalEntity>.toDomain() = map(FestivalEntity::toDomain)

fun FestivalEntity.toDomain() = Festival(
    id = id.toLong(),
    name = name,
    startDate = LocalDate.parse(startDate),
    endDate = LocalDate.parse(endDate),
    thumbnail = thumbnail,
)

fun List<Festival>.toEntity() = map(Festival::toEntity)

fun Festival.toEntity() = FestivalEntity(
    id = this.id.toInt(),
    schoolId = -1,
    name = this.name,
    startDate = this.startDate.toString(),
    endDate = this.endDate.toString(),
    thumbnail = this.thumbnail,
)
