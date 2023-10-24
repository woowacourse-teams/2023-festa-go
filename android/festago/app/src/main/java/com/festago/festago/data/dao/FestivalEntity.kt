package com.festago.festago.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.festago.festago.model.Festival
import java.time.LocalDate

@Entity(tableName = "festivals")
data class FestivalEntity(
    @PrimaryKey
    val id: Int,
    val schoolId: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val thumbnail: String,
) {
    fun toDomain() = Festival(
        id = id.toLong(),
        name = name,
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate),
        thumbnail = thumbnail,
    )

    companion object {
        fun from(festival: Festival) = FestivalEntity(
            id = festival.id.toInt(),
            schoolId = -1,
            name = festival.name,
            startDate = festival.startDate.toString(),
            endDate = festival.endDate.toString(),
            thumbnail = festival.thumbnail,
        )
    }
}
