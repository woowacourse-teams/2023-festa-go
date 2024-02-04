package com.festago.festago.data.repository

import com.festago.festago.domain.model.artist.Artist
import com.festago.festago.domain.model.artist.ArtistDetail
import com.festago.festago.domain.model.artist.ArtistMedia
import com.festago.festago.domain.model.artist.Stage
import com.festago.festago.domain.model.artist.Stages
import com.festago.festago.domain.repository.ArtistRepository
import java.time.LocalDate
import javax.inject.Inject

class FakeArtistRepository @Inject constructor() : ArtistRepository {
    override suspend fun loadArtistDetail(): Result<ArtistDetail> = Result.success(
        ArtistDetail(
            1,
            "뉴진스",
            "https://example.com/artist-logo.jpg",
            "https://example.com/artist-background.jpg",
            listOf(
                ArtistMedia(
                    "https://example.com/instagram-icon.jpg",
                    "공식 인스타그램",
                    "INSTAGRAM",
                    "https://instagram.com/exampleartist"
                ),
                ArtistMedia(
                    "https://example.com/twitter-icon.jpg",
                    "공식 엑스",
                    "X",
                    "https://twitter.com/exampleartist"
                )
            )
        )
    )

    override suspend fun loadArtistStages(size: Int): Result<Stages> = Result.success(
        Stages(
            false,
            listOf(
                Stage(
                    1,
                    "예시 페스티벌 1",
                    LocalDate.parse("2024-05-01"),
                    LocalDate.parse("2024-05-03"),
                    "https://example.com/festival1-image.jpg",
                    listOf(
                        Artist(101, "뉴진스", "https://static.wikia.nocookie.net/witchers/images/d/d9/New_Jeans_Cover.png/revision/latest?cb=20220801091438"),
                        Artist(102, "아티스트 B", "https://i0.wp.com/m.izm.co.kr/wp-content/uploads/2023/04/image-13.png?w=1200&ssl=1")
                    )
                ),
                Stage(
                    2,
                    "예시 페스티벌 2",
                    LocalDate.parse("2024-06-10"),
                    LocalDate.parse("2024-06-12"),
                    "https://example.com/festival2-image.jpg",
                    listOf(
                        Artist(101, "뉴진스", "https://static.wikia.nocookie.net/witchers/images/d/d9/New_Jeans_Cover.png/revision/latest?cb=20220801091438"),
                        Artist(102, "아티스트 B", "https://i0.wp.com/m.izm.co.kr/wp-content/uploads/2023/04/image-13.png?w=1200&ssl=1")
                    )
                )
            )
        )
    )
}

