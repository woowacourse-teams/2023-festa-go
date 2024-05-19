package com.festago.festago.domain.exception

object BookmarkLimitExceededException : Exception() {
    private fun readResolve(): Any = BookmarkLimitExceededException
}

fun Throwable.isBookmarkLimitExceeded() = this is BookmarkLimitExceededException
