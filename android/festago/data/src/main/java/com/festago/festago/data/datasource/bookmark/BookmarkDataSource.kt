package com.festago.festago.data.datasource.bookmark

import com.festago.festago.domain.model.bookmark.BookmarkType

interface BookmarkDataSource {
    fun addBookmark(id: Long, type: BookmarkType)
    fun isBookmarked(id: Long, type: BookmarkType): Boolean
    fun deleteBookmark(id: Long, type: BookmarkType)
    fun getBookmarks(type: BookmarkType): List<Long>
    fun setBookmarks(type: BookmarkType, ids: List<Long>)
}
