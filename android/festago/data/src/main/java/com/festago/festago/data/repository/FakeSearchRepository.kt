package com.festago.festago.data.repository

import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.model.search.ArtistSearch
import com.festago.festago.domain.model.search.SchoolSearch
import com.festago.festago.domain.repository.SearchRepository
import java.time.LocalDate
import javax.inject.Inject

class FakeSearchRepository @Inject constructor() : SearchRepository {
    override suspend fun searchFestivals(searchQuery: String): Result<List<Festival>> {
        return Result.success(FakeFestivals.plannedFestivals)
    }

    override suspend fun searchArtists(searchQuery: String): Result<List<ArtistSearch>> {
        return Result.success(
            listOf(
                ArtistSearch(
                    id = 6L,
                    name = "뉴진스",
                    profileImageUrl = "https://cdn.mediatoday.co.kr/news/photo/202311/313885_438531_4716.jpg",
                    todayStage = 2,
                    upcomingStage = 1,
                ),
                ArtistSearch(
                    id = 1L,
                    name = "BTS",
                    profileImageUrl = "https://i.namu.wiki/i/gpgJvt_C2vKJS4VA4K_Vm57Y5WoS83ofshxhJlQaT4P9Tu0N96vZ2OcdeAN7ZtRAM26UyyQs3sualkKk6i_SrRMvwVKrU015XJqzJ7wKRbOub_oUAxPSFre_8D5De3oy-fCxL0uZ-HGvsWxIX57yrw.webp",
                    todayStage = 2,
                    upcomingStage = 2,
                ),
                ArtistSearch(
                    id = 2L,
                    name = "싸이",
                    profileImageUrl = "https://i.namu.wiki/i/VH58lI8f-y8QSoxFH9IAjjCobySN0lflZ4rMy6Un7qawUwAyi9UfeseZWCzxH-lQeZk7q_eUyTHGlZBAPqSLWliIKWYDLaAgomVtOyAQg60aCpF3oNTBOgUe_hig3rbHW-YAgoj95Fww3MCToyM6MA.webp",
                    todayStage = 2,
                    upcomingStage = 3,
                ),
                ArtistSearch(
                    id = 10L,
                    name = "마마무",
                    profileImageUrl = "https://i.namu.wiki/i/Mre8tXnE40mB9_UwXIwASMEAUSVhHvyjJxXq-lQo40C3bLWYfxXBeai8t6TugyomPjFgxL3VfDA2zn65HlzqPXgTKlvdRl1gJ6PGZLxYYk8Uhk8L6va7zm_etSK5UzVLE56fUATqUCq-6tRQXigmYQ.webp",
                    todayStage = 2,
                    upcomingStage = 4,
                ),
                ArtistSearch(
                    id = 11L,
                    name = "블랙핑크",
                    profileImageUrl = "https://i.namu.wiki/i/VZxRYO8_CXa2QbOSZgttDq5ue5QEu_Fbk1Lwo3qpasLAfS802YExcnmVmDhCq3ONF0ExzhACz_YkZbxOGmIfjuPDZnFo7i0pWaT05NluHRHGfp9NqsAT6WBNb0k5KecOyDvakXk0VH2fUo4ojSwC6g.webp",
                    todayStage = 1,
                    upcomingStage = 5,
                ),
            ),
        )
    }

    override suspend fun searchSchools(searchQuery: String): Result<List<SchoolSearch>> {
        return Result.success(
            listOf(
                SchoolSearch(
                    id = 1L,
                    name = "부경대학고",
                    logoUrl = "https://www.pknu.ac.kr/images/front/sub/univ_logo01.png",
                    upcomingFestivalStartDate = LocalDate.now().plusDays(5L),
                ),
                SchoolSearch(
                    id = 1L,
                    name = "서울대학고",
                    logoUrl = "https://www.snu.ac.kr/webdata/uploads/kor/image/2019/12/about-symbol-bi_lg.png",
                    upcomingFestivalStartDate = LocalDate.now().plusDays(3L),
                ),
                SchoolSearch(
                    id = 1L,
                    name = "서울대학고",
                    logoUrl = "https://i.namu.wiki/i/lw8P8ZIL5-HV94rCgFUq9y4bv_geodMRaDX0uIQ_woQL51W5y04LcpG_vYtNXafpVJYWN0F1MxgZdeqvDyOs5g.svg",
                    upcomingFestivalStartDate = null,
                ),

            ),
        )
    }
}
