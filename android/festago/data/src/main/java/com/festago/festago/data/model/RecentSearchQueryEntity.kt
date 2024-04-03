package com.festago.festago.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.festago.festago.domain.model.recentsearch.RecentSearchQuery

@Entity(
    tableName = "recentSearchQueries",
)
data class RecentSearchQueryEntity(
    @PrimaryKey
    val query: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
) {
    fun toDomain() = RecentSearchQuery(query = query, queriedDate = createdAt)
}
