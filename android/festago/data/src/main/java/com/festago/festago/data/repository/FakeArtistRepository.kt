package com.festago.festago.data.repository

import com.festago.festago.domain.model.artist.Artist
import com.festago.festago.domain.model.artist.ArtistDetail
import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.model.school.School
import com.festago.festago.domain.model.social.SocialMedia
import com.festago.festago.domain.repository.ArtistRepository
import java.time.LocalDate
import javax.inject.Inject

class FakeArtistRepository @Inject constructor() : ArtistRepository {
    var index = 0

    override suspend fun loadArtistDetail(id: Long, delayTimeMillis: Long): Result<ArtistDetail> =
        Result.success(
            ArtistDetail(
                1,
                "뉴진스${index++}",
                "https://static.wikia.nocookie.net/witchers/images/d/d9/New_Jeans_Cover.png/revision/latest?cb=20220801091438",
                "https://static.wikia.nocookie.net/witchers/images/d/d9/New_Jeans_Cover.png/revision/latest?cb=20220801091438",
                listOf(
                    SocialMedia(
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1200px-Instagram_icon.png?20200512141346",
                        "공식 인스타그램",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Instagram_logo_2016.svg/264px-Instagram_logo_2016.svg.png",
                        "https://www.instagram.com/newjeans_official/",
                    ),
                    SocialMedia(
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/X_logo_2023.svg/600px-X_logo_2023.svg.png?20230819000805",
                        "공식 엑스",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/X_logo_2023.svg/531px-X_logo_2023.svg.png",
                        "https://twitter.com/NewJeans_ADOR",
                    ),
                ),
            ),
        )

    override suspend fun loadArtistFestivals(
        id: Long,
        size: Int?,
        lastFestivalId: Long?,
        lastStartDate: LocalDate?,
        isPast: Boolean?,
    ): Result<FestivalsPage> =
        Result.success(
            FestivalsPage(
                isLastPage = false,
                festivals = (0..10).flatMap {
                    listOf(
                        Festival(
                            1,
                            "예시 페스티벌 1",
                            LocalDate.parse("2024-05-01"),
                            LocalDate.parse("2024-05-03"),
                            "https://source.unsplash.com/random/300×${300}",
                            School(
                                1,
                                "예시 학교",
                                "https://source.unsplash.com/random/300×${300 + index++}",
                            ),
                            listOf(
                                Artist(
                                    101,
                                    "뉴진스뉴진스",
                                    "https://static.wikia.nocookie.net/witchers/images/d/d9/New_Jeans_Cover.png/revision/latest?cb=20220801091438",
                                ),
                                Artist(
                                    102,
                                    "아티스트 B",
                                    "https://source.unsplash.com/random/300×${300 + index++}",
                                ),
                            ),
                        ),
                        Festival(
                            2,
                            "예시 페스티벌 2",
                            LocalDate.parse("2024-06-10"),
                            LocalDate.parse("2024-06-12"),
                            "https://source.unsplash.com/random/300×${300 + index++}",
                            School(
                                1,
                                "예시 학교",
                                "https://source.unsplash.com/random/300×${300 + index++}",
                            ),
                            listOf(
                                Artist(
                                    101,
                                    "뉴진스뉴진스뉴진스뉴진스뉴진스",
                                    "https://static.wikia.nocookie.net/witchers/images/d/d9/New_Jeans_Cover.png/revision/latest?cb=20220801091438",
                                ),
                                Artist(
                                    102,
                                    "아티스트 B",
                                    "https://source.unsplash.com/random/300×${300 + index++}",
                                ),
                            ),
                        ),
                    )
                },
            ),
        )
}
