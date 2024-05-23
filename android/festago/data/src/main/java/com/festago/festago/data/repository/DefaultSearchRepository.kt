package com.festago.festago.data.repository

import com.festago.festago.data.service.SearchRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.domain.model.search.ArtistSearch
import com.festago.festago.domain.model.search.FestivalSearch
import com.festago.festago.domain.model.search.SchoolSearch
import com.festago.festago.domain.repository.SearchRepository
import javax.inject.Inject

class DefaultSearchRepository @Inject constructor(
    private val searchRetrofitService: SearchRetrofitService,
) : SearchRepository {

    override suspend fun searchFestivals(searchQuery: String): Result<List<FestivalSearch>> {
        return runCatchingResponse { searchRetrofitService.searchFestivals(searchQuery) }.onSuccessOrCatch { festivalResponses ->
            festivalResponses.map { it.toDomain() }
        }
    }

    override suspend fun searchArtists(searchQuery: String): Result<List<ArtistSearch>> {
        return runCatchingResponse {
            searchRetrofitService.searchArtists(searchQuery)
        }.onSuccessOrCatch { artistSearchResponses -> artistSearchResponses.map { it.toDomain() } }
    }

    override suspend fun searchSchools(searchQuery: String): Result<List<SchoolSearch>> {
        if (searchQuery.length <= MIN_SCHOOL_SEARCH_QUERY_LENGTH) {
            return Result.success(emptyList())
        }
        return runCatchingResponse {
            searchRetrofitService.searchSchools(searchQuery)
        }.onSuccessOrCatch { schoolSearchResponses -> schoolSearchResponses.map { it.toDomain() } }
    }

    companion object {
        const val MIN_SCHOOL_SEARCH_QUERY_LENGTH = 1
    }
}
