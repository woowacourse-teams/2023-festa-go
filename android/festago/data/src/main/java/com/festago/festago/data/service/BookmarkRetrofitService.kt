package com.festago.festago.data.service

import com.festago.festago.data.dto.artist.ArtistDetailResponse
import com.festago.festago.data.dto.bookmark.SchoolBookmarkResponse
import com.festago.festago.data.dto.festival.FestivalsResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface BookmarkRetrofitService {
    @PUT("api/v1/bookmarks")
    suspend fun addBookmark(
        @Query("resourceId") resourceId: Long,
        @Query("bookmarkType") bookmarkType: BookmarkType,
    ): Response<Unit>

    @DELETE("api/v1/bookmarks")
    suspend fun deleteBookmark(
        @Query("resourceId") resourceId: Long,
        @Query("bookmarkType") bookmarkType: BookmarkType,
    ): Response<Unit>

    @GET("api/v1/bookmarks/schools")
    suspend fun getSchoolBookmarks(): Response<SchoolBookmarkResponse>

    @GET("api/v1/bookmarks/festivals/ids")
    suspend fun getFestivalBookmarkIds(): Response<List<Long>>

    @GET("api/v1/bookmarks/festivals")
    suspend fun getFestivalBookmarks(
        @Query("festivalIds") festivalIds: List<Long>,
        @Query("festivalBookmarkOrder") festivalBookmarkOrder: FestivalBookmarkOrder,
    ): Response<FestivalsResponse>

    @GET("api/v1/bookmarks/artists")
    suspend fun getBookmarks(): Response<ArtistDetailResponse>
}

enum class FestivalBookmarkOrder {
    BOOKMARK,
    FESTIVAL,
}

enum class BookmarkType {
    ARTIST,
    FESTIVAL,
    SCHOOL,
}
