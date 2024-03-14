package com.festago.festago.data.repository

import com.festago.festago.data.dao.RecentSearchQueryDao
import com.festago.festago.data.model.RecentSearchQueryEntity
import com.festago.festago.domain.model.recentsearch.RecentSearchQuery
import com.festago.festago.domain.repository.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultRecentSearchRepository @Inject constructor(
    private val recentSearchQueryDao: RecentSearchQueryDao,
) : RecentSearchRepository {

    override suspend fun insertOrReplaceRecentSearch(searchQuery: String) {
        recentSearchQueryDao.insertOrReplaceRecentSearchQuery(
            RecentSearchQueryEntity(
                query = searchQuery,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    override suspend fun deleteRecentSearch(searchQuery: String) {
        recentSearchQueryDao.deleteRecentSearchQuery(
            RecentSearchQueryEntity(
                query = searchQuery,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    override suspend fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>> {
        return recentSearchQueryDao.getRecentSearchQueryEntities(limit).map { recentSearchQueries ->
            recentSearchQueries.map { it.toDomain() }
        }
    }

    override suspend fun clearRecentSearches() = recentSearchQueryDao.clearRecentSearchQueries()
}
