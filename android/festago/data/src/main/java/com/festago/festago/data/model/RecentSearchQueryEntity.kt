package com.festago.festago.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.festago.festago.domain.model.recentsearch.RecentSearchQuery
import kotlinx.datetime.Instant

@Entity(
    tableName = "recentSearchQueries",
)
data class RecentSearchQueryEntity(
    @PrimaryKey
    val query: String,
    @ColumnInfo
    val queriedDate: Instant,
) {
    fun toDomain() = RecentSearchQuery(query = query, queriedDate = queriedDate)
}
