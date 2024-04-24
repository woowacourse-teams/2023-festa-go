package com.festago.festago.domain.repository

import com.festago.festago.domain.model.bookmark.ArtistBookmark
import com.festago.festago.domain.model.bookmark.FestivalBookmark
import com.festago.festago.domain.model.bookmark.FestivalBookmarkOrder
import com.festago.festago.domain.model.bookmark.SchoolBookmark

interface BookmarkRepository {
    // Festival Bookmark
    suspend fun addFestivalBookmark(festivalId: Long): Result<Unit>

    suspend fun getFestivalBookmarks(
        festivalIds: List<Long>,
        festivalBookmarkOrder: FestivalBookmarkOrder,
    ): Result<List<FestivalBookmark>>

    suspend fun getFestivalBookmarkIds(): Result<List<Long>>

    suspend fun deleteFestivalBookmark(festivalId: Long): Result<Unit>

    // School Bookmark
    suspend fun addSchoolBookmark(schoolId: Long): Result<Unit>

    suspend fun getSchoolBookmarks(): Result<List<SchoolBookmark>>

    suspend fun deleteSchoolBookmark(schoolId: Long): Result<Unit>

    // Artist Bookmark
    suspend fun addArtistBookmark(artistId: Long): Result<Unit>

    suspend fun getArtistBookmarks(): Result<List<ArtistBookmark>>

    suspend fun deleteArtistBookmark(artistId: Long): Result<Unit>
}
