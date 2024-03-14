package com.festago.festago.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.festago.festago.data.model.RecentSearchQueryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchQueryDao {
    @Query(value = "SELECT * FROM recentSearchQueries ORDER BY created_at DESC LIMIT :limit")
    fun getRecentSearchQueryEntities(limit: Int): Flow<List<RecentSearchQueryEntity>>

    @Upsert
    suspend fun insertOrReplaceRecentSearchQuery(recentSearchQuery: RecentSearchQueryEntity)

    @Delete
    suspend fun deleteRecentSearchQuery(recentSearchQuery: RecentSearchQueryEntity)

    @Query(value = "DELETE FROM recentSearchQueries")
    suspend fun clearRecentSearchQueries()
}
