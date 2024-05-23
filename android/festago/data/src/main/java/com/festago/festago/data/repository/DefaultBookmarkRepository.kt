package com.festago.festago.data.repository

import com.festago.festago.data.datasource.bookmark.BookmarkDataSource
import com.festago.festago.data.service.BookmarkRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.domain.model.bookmark.ArtistBookmark
import com.festago.festago.domain.model.bookmark.BookmarkType
import com.festago.festago.domain.model.bookmark.FestivalBookmark
import com.festago.festago.domain.model.bookmark.FestivalBookmarkOrder
import com.festago.festago.domain.model.bookmark.SchoolBookmark
import com.festago.festago.domain.repository.BookmarkRepository
import javax.inject.Inject

class DefaultBookmarkRepository @Inject constructor(
    private val bookmarkRetrofitService: BookmarkRetrofitService,
    private val bookmarkDataSource: BookmarkDataSource,
) : BookmarkRepository {
    override suspend fun addFestivalBookmark(festivalId: Long): Result<Unit> {
        return runCatchingResponse {
            bookmarkRetrofitService.addBookmark(festivalId, BookmarkType.FESTIVAL)
        }.onSuccessOrCatch {
            bookmarkDataSource.addBookmark(festivalId, BookmarkType.FESTIVAL)
        }
    }

    override suspend fun getFestivalBookmarks(
        festivalIds: List<Long>,
        festivalBookmarkOrder: FestivalBookmarkOrder,
    ): Result<List<FestivalBookmark>> {
        return runCatchingResponse {
            bookmarkRetrofitService.getFestivalBookmarks(
                festivalIds = festivalIds,
                festivalBookmarkOrder = festivalBookmarkOrder,
            )
        }.onSuccessOrCatch { response ->
            response.map { festival -> festival.toDomain() }.also { festivalBookmarks ->
                bookmarkDataSource.setBookmarks(
                    BookmarkType.FESTIVAL,
                    festivalBookmarks.map { it.festival.id },
                )
            }
        }
    }

    override suspend fun getFestivalBookmarkIds(): Result<List<Long>> {
        return runCatchingResponse {
            bookmarkRetrofitService.getFestivalBookmarkIds()
        }.onSuccessOrCatch { it.also { festivalIds -> storeFestivalBookmarks(festivalIds) } }
    }

    override suspend fun deleteFestivalBookmark(festivalId: Long): Result<Unit> {
        val result = runCatchingResponse {
            bookmarkRetrofitService.deleteBookmark(festivalId, BookmarkType.FESTIVAL)
        }
        result.onSuccess { bookmarkDataSource.deleteBookmark(festivalId, BookmarkType.FESTIVAL) }
        result.onFailure { if (it.message?.contains("204") == true) return Result.success(Unit) }
        return result
    }

    override suspend fun addSchoolBookmark(schoolId: Long): Result<Unit> {
        return runCatchingResponse {
            bookmarkRetrofitService.addBookmark(schoolId, BookmarkType.SCHOOL)
        }.onSuccessOrCatch {
            bookmarkDataSource.addBookmark(schoolId, BookmarkType.SCHOOL)
        }
    }

    override suspend fun getSchoolBookmarks(): Result<List<SchoolBookmark>> {
        return runCatchingResponse {
            bookmarkRetrofitService.getSchoolBookmarks()
        }.onSuccessOrCatch { response ->
            response.map { schools -> schools.toDomain() }
                .also { schoolBookmarks -> storeSchoolBookmarks(schoolBookmarks) }
        }
    }

    override suspend fun deleteSchoolBookmark(schoolId: Long): Result<Unit> {
        val result = runCatchingResponse {
            bookmarkRetrofitService.deleteBookmark(schoolId, BookmarkType.SCHOOL)
        }
        result.onSuccess { bookmarkDataSource.deleteBookmark(schoolId, BookmarkType.SCHOOL) }
        result.onFailure { if (it.message?.contains("204") == true) return Result.success(Unit) }
        return result
    }

    override suspend fun addArtistBookmark(artistId: Long): Result<Unit> {
        return runCatchingResponse {
            bookmarkRetrofitService.addBookmark(artistId, BookmarkType.ARTIST)
        }.onSuccessOrCatch { bookmarkDataSource.addBookmark(artistId, BookmarkType.ARTIST) }
    }

    override suspend fun getArtistBookmarks(): Result<List<ArtistBookmark>> {
        return runCatchingResponse {
            bookmarkRetrofitService.getArtistBookmarks()
        }.onSuccessOrCatch {
            it.map { artist -> artist.toDomain() }
                .also { artistBookmarks -> storeArtistBookmarks(artistBookmarks) }
        }.onFailure {
            it.printStackTrace()
        }
    }

    override suspend fun deleteArtistBookmark(artistId: Long): Result<Unit> {
        val result = runCatchingResponse {
            bookmarkRetrofitService.deleteBookmark(artistId, BookmarkType.ARTIST)
        }
        result.onSuccess { bookmarkDataSource.deleteBookmark(artistId, BookmarkType.ARTIST) }
        result.onFailure { if (it.message?.contains("204") == true) return Result.success(Unit) }
        return result
    }

    override fun isBookmarked(id: Long, type: BookmarkType): Boolean {
        return bookmarkDataSource.isBookmarked(id, type)
    }

    private fun storeSchoolBookmarks(schoolBookmarks: List<SchoolBookmark>) {
        bookmarkDataSource.setBookmarks(
            BookmarkType.SCHOOL,
            schoolBookmarks.map { it.school.id },
        )
    }

    private fun storeArtistBookmarks(artistBookmarks: List<ArtistBookmark>) {
        bookmarkDataSource.setBookmarks(
            BookmarkType.ARTIST,
            artistBookmarks.map { it.artist.id },
        )
    }

    private fun storeFestivalBookmarks(festivalBookmarks: List<Long>) {
        bookmarkDataSource.setBookmarks(BookmarkType.FESTIVAL, festivalBookmarks)
    }
}
