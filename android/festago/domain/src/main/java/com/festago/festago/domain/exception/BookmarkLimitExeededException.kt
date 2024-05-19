package com.festago.festago.domain.exception

class BookmarkLimitExceededException : Exception()

fun Throwable.isBookmarkLimitExceeded() = this is BookmarkLimitExceededException
