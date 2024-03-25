package com.festago.festago.domain.repository

import com.festago.festago.domain.model.recentsearch.RecentSearchQuery

interface RecentSearchRepository {
    suspend fun insertOrReplaceRecentSearch(searchQuery: String)
    suspend fun deleteRecentSearch(searchQuery: String)
    suspend fun getRecentSearchQueries(limit: Int): List<RecentSearchQuery>
    suspend fun clearRecentSearches()
}
