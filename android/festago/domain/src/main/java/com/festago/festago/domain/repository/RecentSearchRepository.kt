package com.festago.festago.domain.repository

import com.festago.festago.domain.model.recentsearch.RecentSearchQuery
import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {
    suspend fun insertOrReplaceRecentSearch(searchQuery: String)
    suspend fun deleteRecentSearch(searchQuery: String)
    suspend fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>>
    suspend fun clearRecentSearches()
}
