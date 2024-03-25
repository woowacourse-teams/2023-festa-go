package com.festago.festago.domain.repository

import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.model.search.ArtistSearch
import com.festago.festago.domain.model.search.SchoolSearch

interface SearchRepository {
    suspend fun searchFestivals(searchQuery: String): Result<List<Festival>>
    suspend fun searchArtists(searchQuery: String): Result<List<ArtistSearch>>
    suspend fun searchSchools(searchQuery: String): Result<List<SchoolSearch>>
}
