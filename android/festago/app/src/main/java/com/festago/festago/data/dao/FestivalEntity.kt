package com.festago.festago.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "festivals")
data class FestivalEntity(
    @PrimaryKey
    val id: Int,
    val schoolId: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val thumbnail: String,
)
