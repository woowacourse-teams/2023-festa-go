package com.festago.festago.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.festago.festago.data.model.RecentSearchQueryEntity

@Dao
interface RecentSearchQueryDao {
    @Query(value = "SELECT * FROM recentSearchQueries ORDER BY queriedDate DESC LIMIT :limit")
    fun getRecentSearchQueryEntities(limit: Int): List<RecentSearchQueryEntity>

    @Upsert
    suspend fun insertOrReplaceRecentSearchQuery(recentSearchQuery: RecentSearchQueryEntity)

    @Delete
    fun deleteRecentSearchQuery(recentSearchQuery: RecentSearchQueryEntity)

    @Query(value = "DELETE FROM recentSearchQueries")
    suspend fun clearRecentSearchQueries()
}
