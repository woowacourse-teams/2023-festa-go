package com.festago.festago.data.repository

import com.festago.festago.domain.model.bookmark.ArtistBookmark
import com.festago.festago.domain.model.bookmark.ArtistBookmarkInfo
import com.festago.festago.domain.model.bookmark.FestivalBookmark
import com.festago.festago.domain.model.bookmark.FestivalBookmarkOrder
import com.festago.festago.domain.model.bookmark.SchoolBookmark
import com.festago.festago.domain.repository.BookmarkRepository
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import javax.inject.Inject

class FakeBookmarkRepository @Inject constructor() : BookmarkRepository {
    override suspend fun addFestivalBookmark(festivalId: Long): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getFestivalBookmarks(
        festivalIds: List<Long>,
        festivalBookmarkOrder: FestivalBookmarkOrder,
    ): Result<List<FestivalBookmark>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFestivalBookmarkIds(): Result<List<Long>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFestivalBookmark(festivalId: Long): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addSchoolBookmark(schoolId: Long): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getSchoolBookmarks(): Result<List<SchoolBookmark>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSchoolBookmark(schoolId: Long): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addArtistBookmark(artistId: Long): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getArtistBookmarks(): Result<List<ArtistBookmark>> {
        delay(1000)
        return Result.failure(Exception("Failed to get artist bookmarks"))
        return Result.success(
            listOf(
                ArtistBookmark(
                    ArtistBookmarkInfo(
                        name = "Artist 1",
                        profileImageUrl = "https://picsum.photos/200/300",
                    ),
                    LocalDateTime.now(),
                ),
            ),
        )
    }

    override suspend fun deleteArtistBookmark(artistId: Long): Result<Unit> {
        TODO("Not yet implemented")
    }
}
