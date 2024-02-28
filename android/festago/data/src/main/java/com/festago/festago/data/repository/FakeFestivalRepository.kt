package com.festago.festago.data.repository

import com.festago.festago.domain.model.artist.Artist
import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.model.festival.FestivalDetail
import com.festago.festago.domain.model.festival.FestivalFilter
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.model.festival.PopularFestivals
import com.festago.festago.domain.model.festival.SchoolRegion
import com.festago.festago.domain.model.school.School
import com.festago.festago.domain.model.social.SocialMedia
import com.festago.festago.domain.model.stage.Stage
import com.festago.festago.domain.repository.FestivalRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class FakeFestivalRepository @Inject constructor() : FestivalRepository {

    override suspend fun loadPopularFestivals(): Result<PopularFestivals> {
        return Result.success(PopularFestivals("인기 축제 목록", FakeFestivals.popularFestivals))
    }

    override suspend fun loadFestivals(
        schoolRegion: SchoolRegion?,
        festivalFilter: FestivalFilter?,
        lastFestivalId: Long?,
        lastStartDate: LocalDate?,
        size: Int?,
    ): Result<FestivalsPage> {
        val notNullSize = size ?: DEFAULT_SIZE
        val notNullLastFestivalId = lastFestivalId ?: DEFAULT_LAST_FESTIVAL_ID

        if (notNullLastFestivalId + notNullSize < LAST_ITEM_ID) {
            return Result.success(
                FestivalsPage(
                    false,
                    getFestivals((notNullLastFestivalId + 1)..(notNullLastFestivalId + notNullSize)),
                ),
            )
        }
        return Result.success(
            FestivalsPage(
                true,
                getFestivals((notNullLastFestivalId + 1)..LAST_ITEM_ID),
            ),
        )
    }

    private fun getFestivals(idRange: LongRange): List<Festival> {
        return (idRange).map { id ->
            Festival(
                id = id,
                name = "뉴진스 콘서트 $id",
                startDate = LocalDate.MIN,
                endDate = LocalDate.MAX,
                imageUrl = "data:image/jpeg;base64,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",
                school = School(id = 1L, name = "고려대", imageUrl = ""),
                artists = listOf(
                    Artist(
                        id = 6L,
                        name = "뉴진스",
                        imageUrl = "https://cdn.mediatoday.co.kr/news/photo/202311/313885_438531_4716.jpg",
                    ),
                    Artist(
                        id = 1L,
                        name = "BTS",
                        imageUrl = "https://i.namu.wiki/i/gpgJvt_C2vKJS4VA4K_Vm57Y5WoS83ofshxhJlQaT4P9Tu0N96vZ2OcdeAN7ZtRAM26UyyQs3sualkKk6i_SrRMvwVKrU015XJqzJ7wKRbOub_oUAxPSFre_8D5De3oy-fCxL0uZ-HGvsWxIX57yrw.webp",
                    ),
                    Artist(
                        id = 2L,
                        name = "싸이",
                        imageUrl = "https://i.namu.wiki/i/VH58lI8f-y8QSoxFH9IAjjCobySN0lflZ4rMy6Un7qawUwAyi9UfeseZWCzxH-lQeZk7q_eUyTHGlZBAPqSLWliIKWYDLaAgomVtOyAQg60aCpF3oNTBOgUe_hig3rbHW-YAgoj95Fww3MCToyM6MA.webp",
                    ),
                    Artist(
                        id = 10L,
                        name = "마마무",
                        imageUrl = "https://i.namu.wiki/i/Mre8tXnE40mB9_UwXIwASMEAUSVhHvyjJxXq-lQo40C3bLWYfxXBeai8t6TugyomPjFgxL3VfDA2zn65HlzqPXgTKlvdRl1gJ6PGZLxYYk8Uhk8L6va7zm_etSK5UzVLE56fUATqUCq-6tRQXigmYQ.webp",
                    ),
                    Artist(
                        id = 11L,
                        name = "블랙핑크",
                        imageUrl = "https://i.namu.wiki/i/VZxRYO8_CXa2QbOSZgttDq5ue5QEu_Fbk1Lwo3qpasLAfS802YExcnmVmDhCq3ONF0ExzhACz_YkZbxOGmIfjuPDZnFo7i0pWaT05NluHRHGfp9NqsAT6WBNb0k5KecOyDvakXk0VH2fUo4ojSwC6g.webp",
                    ),
                ),
            )
        }
    }

    override suspend fun loadFestivalDetail(id: Long): Result<FestivalDetail> {
        return Result.success(
            FestivalDetail(
                id = 1L,
                name = "대동제",
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(5L),
                posterImageUrl = "https://mblogthumb-phinf.pstatic.net/MjAyMzA1MjNfMTMx/MDAxNjg0ODIwNzY5NzQ5.MuYItN1HCOQUcADB6B7ua0SO9Au_QNNk01-6yZkcTH0g.wxSjluY-Glq20JIojs7OuScLQWh6c_sQsoW5xXqiM7Ag.JPEG.chummilmil99/SE-126908ba-0f82-4903-91c5-695db78a98e9.jpg?type=w800",
                school = School(id = 2L, name = "부경대학교", imageUrl = ""),
                socialMedias = listOf(
                    SocialMedia(
                        type = "INSTAGRAM",
                        name = "총학생회 인스타그램",
                        logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Instagram_logo_2016.svg/2048px-Instagram_logo_2016.svg.png",
                        url = "https://www.instagram.com/25th_solution/",
                    ),
                    SocialMedia(
                        type = "FACEBOOK",
                        name = "총학생회 페이스북",
                        logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Facebook_f_logo_%282019%29.svg/1200px-Facebook_f_logo_%282019%29.svg.png",
                        url = "https://www.facebook.com/23rdemotion/",
                    ),
                ),
                stages = listOf(
                    Stage(
                        id = 1L,
                        startDateTime = LocalDateTime.now().plusDays(0L),
                        artists = listOf(
                            Artist(
                                id = 1L,
                                name = "BTS",
                                imageUrl = "https://i.namu.wiki/i/gpgJvt_C2vKJS4VA4K_Vm57Y5WoS83ofshxhJlQaT4P9Tu0N96vZ2OcdeAN7ZtRAM26UyyQs3sualkKk6i_SrRMvwVKrU015XJqzJ7wKRbOub_oUAxPSFre_8D5De3oy-fCxL0uZ-HGvsWxIX57yrw.webp",
                            ),
                            Artist(
                                id = 2L,
                                name = "싸이",
                                imageUrl = "https://i.namu.wiki/i/VH58lI8f-y8QSoxFH9IAjjCobySN0lflZ4rMy6Un7qawUwAyi9UfeseZWCzxH-lQeZk7q_eUyTHGlZBAPqSLWliIKWYDLaAgomVtOyAQg60aCpF3oNTBOgUe_hig3rbHW-YAgoj95Fww3MCToyM6MA.webp",
                            ),
                            Artist(
                                id = 10L,
                                name = "마마무",
                                imageUrl = "https://i.namu.wiki/i/Mre8tXnE40mB9_UwXIwASMEAUSVhHvyjJxXq-lQo40C3bLWYfxXBeai8t6TugyomPjFgxL3VfDA2zn65HlzqPXgTKlvdRl1gJ6PGZLxYYk8Uhk8L6va7zm_etSK5UzVLE56fUATqUCq-6tRQXigmYQ.webp",
                            ),
                            Artist(
                                id = 11L,
                                name = "블랙핑크",
                                imageUrl = "https://i.namu.wiki/i/VZxRYO8_CXa2QbOSZgttDq5ue5QEu_Fbk1Lwo3qpasLAfS802YExcnmVmDhCq3ONF0ExzhACz_YkZbxOGmIfjuPDZnFo7i0pWaT05NluHRHGfp9NqsAT6WBNb0k5KecOyDvakXk0VH2fUo4ojSwC6g.webp",
                            ),
                            Artist(
                                id = 4L,
                                name = "AKMU",
                                imageUrl = "https://i.namu.wiki/i/7yRF8Yrk9kdQxzETNO8TQp9jJpQENVUGbj-4YwB-xdVmJWoTAY7MgVA6G72Z-xmunPG0Zd3WTN_EsTwsx7oNFIO-yl0nHmaIU-ZRCpyhzVE5L9y8Sb9gkAKVt_jZBtgvVrOjw1UQq32gQsYaoS1jsg.webp",
                            ),
                        ),
                    ),
                    Stage(
                        id = 2L,
                        startDateTime = LocalDateTime.now().plusDays(1L),
                        artists = listOf(
                            Artist(
                                id = 3L,
                                name = "아이유",
                                imageUrl = "https://i.namu.wiki/i/-GuxB5nI9Q-a5W_nAJEapwdUzCLyFShWJfmUfZk04cW_fFC485TRD6UlzGQCBnFpJegXBaa4WO-PThNom_7wlosOiXgb-k3-wgUr3PkyX89PU3RCschmgQ0FmS1ClOK3ph4ztAd55YWWlhk7Gm114w.webp",
                            ),
                            Artist(
                                id = 5L,
                                name = "뉴진스",
                                imageUrl = "https://i.namu.wiki/i/GdMUzQlsrAXyF5zlgqRR0lYvAGnFghBbLxqTZK_mzLvV0LYPNQdaak1ezYtKqSNBA7UaINkrMNqncRkxThI8j2IEk2qcXJ3bLqIllRexenai641g-uvxCxFcDa9doCy0kTnMLEp5gad8Ze2fLDDBvg.webp",
                            ),
                            Artist(
                                id = 6L,
                                name = "비비",
                                imageUrl = "https://i.namu.wiki/i/JlXBTAah7fOILgmvAQf5bW4yWbS082qw6XtV36g4a-2g5TrwTRaUf95r1YnEYi6dt_rf3o9YuRN2qVl0pdgIW5d6-DeYg67KwaSrqu3_MkUwQItlsrSLqDjm1G0jW-Z5mzQ2aOTU4ZvyE1hpSokIOA.webp",
                            ),
                        ),
                    ),
                    Stage(
                        id = 3L,
                        startDateTime = LocalDateTime.now().plusDays(2L),
                        artists = listOf(
                            Artist(
                                id = 7L,
                                name = "TWS",
                                imageUrl = "https://i.namu.wiki/i/rVwKhMepUc-b-hRa2Nc6mIJRO0eTfgxyAEwVS5XfADNRhQhYJdSg8ke3o6VZd3rLyNasMlGjuXJWqHDoD_Z24o3dBzkaf7gqhCc89XoCKOiII4P-eilx46XHOOTfd2eaonCVNQevsAVl0l5WIWaI5Q.webp",
                            ),
                            Artist(
                                id = 8L,
                                name = "소녀시대",
                                imageUrl = "https://i.namu.wiki/i/snftu-N6Op26hU4HITlraWW6Q_WiSXqhRX2NOhQadzI81RPC7054_mi-evsqRTdRe9nKcBEF-Ugji4vtWunmtiEY1v319tHhIVestCkcSJ0MZF6KbKOScoDjOypW7WPa58goYA-vX5D8baIa2UYFZg.webp",
                            ),
                            Artist(
                                id = 9L,
                                name = "르세라핌",
                                imageUrl = "https://i.namu.wiki/i/Zbm1DseL0fjSd9H2uLrfL9SpBLPYQe7j4S9BPI2wdTw9G_Gykifyw-Nil8yVZglxxW-CRQt15b-tMdrvfuUiSW9mm2ZEBf8sQQQgp9wZmZhe8neg_5A6ehJ6hYLATAqvnOw157aODDq4qU1J-kv-bA.webp",
                            ),
                        ),
                    ),
                    Stage(
                        id = 4L,
                        startDateTime = LocalDateTime.now().plusDays(3L),
                        artists = listOf(
                            Artist(
                                id = 7L,
                                name = "TWS",
                                imageUrl = "https://i.namu.wiki/i/rVwKhMepUc-b-hRa2Nc6mIJRO0eTfgxyAEwVS5XfADNRhQhYJdSg8ke3o6VZd3rLyNasMlGjuXJWqHDoD_Z24o3dBzkaf7gqhCc89XoCKOiII4P-eilx46XHOOTfd2eaonCVNQevsAVl0l5WIWaI5Q.webp",
                            ),
                            Artist(
                                id = 8L,
                                name = "소녀시대",
                                imageUrl = "https://i.namu.wiki/i/snftu-N6Op26hU4HITlraWW6Q_WiSXqhRX2NOhQadzI81RPC7054_mi-evsqRTdRe9nKcBEF-Ugji4vtWunmtiEY1v319tHhIVestCkcSJ0MZF6KbKOScoDjOypW7WPa58goYA-vX5D8baIa2UYFZg.webp",
                            ),
                            Artist(
                                id = 9L,
                                name = "르세라핌",
                                imageUrl = "https://i.namu.wiki/i/Zbm1DseL0fjSd9H2uLrfL9SpBLPYQe7j4S9BPI2wdTw9G_Gykifyw-Nil8yVZglxxW-CRQt15b-tMdrvfuUiSW9mm2ZEBf8sQQQgp9wZmZhe8neg_5A6ehJ6hYLATAqvnOw157aODDq4qU1J-kv-bA.webp",
                            ),
                        ),
                    ),
                    Stage(
                        id = 5L,
                        startDateTime = LocalDateTime.now().plusDays(4L),
                        artists = listOf(
                            Artist(
                                id = 7L,
                                name = "TWS",
                                imageUrl = "https://i.namu.wiki/i/rVwKhMepUc-b-hRa2Nc6mIJRO0eTfgxyAEwVS5XfADNRhQhYJdSg8ke3o6VZd3rLyNasMlGjuXJWqHDoD_Z24o3dBzkaf7gqhCc89XoCKOiII4P-eilx46XHOOTfd2eaonCVNQevsAVl0l5WIWaI5Q.webp",
                            ),
                            Artist(
                                id = 8L,
                                name = "소녀시대",
                                imageUrl = "https://i.namu.wiki/i/snftu-N6Op26hU4HITlraWW6Q_WiSXqhRX2NOhQadzI81RPC7054_mi-evsqRTdRe9nKcBEF-Ugji4vtWunmtiEY1v319tHhIVestCkcSJ0MZF6KbKOScoDjOypW7WPa58goYA-vX5D8baIa2UYFZg.webp",
                            ),
                            Artist(
                                id = 9L,
                                name = "르세라핌",
                                imageUrl = "https://i.namu.wiki/i/Zbm1DseL0fjSd9H2uLrfL9SpBLPYQe7j4S9BPI2wdTw9G_Gykifyw-Nil8yVZglxxW-CRQt15b-tMdrvfuUiSW9mm2ZEBf8sQQQgp9wZmZhe8neg_5A6ehJ6hYLATAqvnOw157aODDq4qU1J-kv-bA.webp",
                            ),
                        ),
                    ),
                ),
            ),
        )
    }

    companion object {
        private const val LAST_ITEM_ID = 27L
        private const val DEFAULT_SIZE = 10
        private const val DEFAULT_LAST_FESTIVAL_ID = -1L
    }
}
