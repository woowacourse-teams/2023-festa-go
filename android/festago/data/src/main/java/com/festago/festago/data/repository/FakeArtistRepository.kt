package com.festago.festago.data.repository

import com.festago.festago.domain.model.artist.Artist
import com.festago.festago.domain.model.artist.ArtistDetail
import com.festago.festago.domain.model.artist.ArtistMedia
import com.festago.festago.domain.model.artist.Stage
import com.festago.festago.domain.model.artist.Stages
import com.festago.festago.domain.repository.ArtistRepository
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
                    "2024-05-01",
                    "2024-05-03",
                    "https://example.com/festival1-image.jpg",
                    listOf(
                        Artist(101, "아티스트 A", "https://example.com/artistA-profile.jpg"),
                        Artist(102, "아티스트 B", "https://example.com/artistB-profile.jpg")
                    )
                ),
                Stage(
                    2,
                    "예시 페스티벌 2",
                    "2024-06-10",
                    "2024-06-12",
                    "https://example.com/festival2-image.jpg",
                    listOf(
                        Artist(201, "아티스트 X", "https://example.com/artistX-profile.jpg"),
                        Artist(202, "아티스트 Y", "https://example.com/artistY-profile.jpg")
                    )
                )
            )
        )
    )
}

