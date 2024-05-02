package com.festago.festago.data.datasource.bookmark

import com.festago.festago.domain.model.bookmark.BookmarkType
import javax.inject.Inject

class DefaultBookMarkDataSource @Inject constructor() : BookmarkDataSource {
    private val bookmarkStore = mutableMapOf<BookmarkType, List<Long>>()

    override fun addBookmark(id: Long, type: BookmarkType) {
        bookmarkStore[type] = (bookmarkStore[type] ?: listOf()) + id
    }

    override fun isBookmarked(id: Long, type: BookmarkType): Boolean {
        bookmarkStore[type]?.let { return it.contains(id) }
        return false
    }

    override fun deleteBookmark(id: Long, type: BookmarkType) {
        val bookmarks = bookmarkStore[type] ?: return
        bookmarkStore[type] = bookmarks - id
    }

    override fun getBookmarks(type: BookmarkType): List<Long> {
        return bookmarkStore[type] ?: listOf()
    }

    override fun setBookmarks(type: BookmarkType, ids: List<Long>) {
        bookmarkStore[type] = ids
    }
}
