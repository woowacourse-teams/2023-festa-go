package com.festago.festago.presentation.ui.artistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.domain.exception.isBookmarkLimitExceeded
import com.festago.festago.domain.exception.isNetworkError
import com.festago.festago.domain.exception.isUnauthorized
import com.festago.festago.domain.model.bookmark.BookmarkType
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.repository.ArtistRepository
import com.festago.festago.domain.repository.BookmarkRepository
import com.festago.festago.domain.repository.UserRepository
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailEvent.BookmarkFailure
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailEvent.BookmarkSuccess
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailEvent.ShowArtistDetail
import com.festago.festago.presentation.ui.artistdetail.ArtistDetailEvent.ShowFestivalDetail
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistDetailUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.FestivalItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val userRepository: UserRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    private val _event: MutableSharedFlow<ArtistDetailEvent> = MutableSharedFlow()
    val event: SharedFlow<ArtistDetailEvent> = _event.asSharedFlow()

    private val _uiState: MutableStateFlow<ArtistDetailUiState> =
        MutableStateFlow(ArtistDetailUiState.Loading)
    val uiState: StateFlow<ArtistDetailUiState> = _uiState.asStateFlow()

    fun loadArtistDetail(id: Long, delayTimeMillis: Long, refresh: Boolean = false) {
        if (!refresh && _uiState.value is ArtistDetailUiState.Success) return

        viewModelScope.launch {
            runCatching {
                val deferredArtistDetail =
                    async { artistRepository.loadArtistDetail(id, delayTimeMillis) }
                val deferredFestivals = async {
                    artistRepository.loadArtistFestivals(id = id, size = FESTIVAL_PAGE_SIZE)
                }

                val artist = deferredArtistDetail.await().getOrThrow()
                val festivalPage = deferredFestivals.await().getOrThrow()

                val isBookmarked = if (userRepository.isSigned()) {
                    bookmarkRepository.isBookmarked(id, BookmarkType.ARTIST)
                } else {
                    false
                }

                _uiState.value = ArtistDetailUiState.Success(
                    artist = artist,
                    bookMarked = isBookmarked,
                    festivals = festivalPage.toUiState(),
                    festivalPage.isLastPage,
                    onBookmarkClick = ::toggleArtistBookmark,
                )

                if (!festivalPage.isLastPage) {
                    return@launch
                }
                val lastFestival = festivalPage.festivals.lastOrNull()
                if (lastFestival == null || lastFestival.endDate >= LocalDate.now()) {
                    loadMoreArtistFestivals(id)
                    return@launch
                }
            }.onFailure {
                handleFailure(key = KEY_LOAD_ARTIST_DETAIL, throwable = it)
            }
        }
    }

    fun loadMoreArtistFestivals(artistId: Long) {
        val successUiState = uiState.value as? ArtistDetailUiState.Success ?: return

        viewModelScope.launch {
            val currentFestivals = successUiState.festivals
            val lastItem = currentFestivals.lastOrNull()
            val isPast =
                lastItem?.endDate?.isBefore(LocalDate.now()) ?: true || successUiState.isLast

            val lastFestivalId = if (successUiState.isLast) null else lastItem?.id
            val lastStartDate = if (successUiState.isLast) null else lastItem?.startDate

            artistRepository.loadArtistFestivals(
                id = artistId,
                lastFestivalId = lastFestivalId,
                lastStartDate = lastStartDate,
                isPast = isPast,
                size = FESTIVAL_PAGE_SIZE,
            ).onSuccess { festivalsPage ->
                _uiState.value = successUiState.copy(
                    festivals = currentFestivals + festivalsPage.toUiState(),
                    isLast = festivalsPage.isLastPage,
                )
            }.onFailure {
                handleFailure(key = KEY_LOAD_MORE_ARTIST_FESTIVAL, throwable = it)
            }
        }
    }

    private fun toggleArtistBookmark(artistId: Int) {
        viewModelScope.launch {
            val uiState = uiState.value as? ArtistDetailUiState.Success ?: return@launch

            if (uiState.bookMarked) {
                _uiState.value = uiState.copy(bookMarked = false)
                bookmarkRepository.deleteArtistBookmark(artistId.toLong())
                    .onSuccess { _event.emit(BookmarkSuccess(false)) }
                    .onFailure {
                        if (it.isUnauthorized()) {
                            _event.emit(BookmarkFailure("로그인이 필요해요"))
                        }
                        if (it.isNetworkError()) {
                            _uiState.value = uiState.copy(bookMarked = true)
                            _event.emit(BookmarkFailure("인터넷 연결을 확인해주세요"))
                        }
                    }
            } else {
                _uiState.value = uiState.copy(bookMarked = true)
                bookmarkRepository.addArtistBookmark(artistId.toLong())
                    .onSuccess { _event.emit(BookmarkSuccess(true)) }
                    .onFailure {
                        if (it.isUnauthorized()) {
                            _event.emit(BookmarkFailure("로그인이 필요해요"))
                        }
                        if (it.isBookmarkLimitExceeded()) {
                            _uiState.value = uiState.copy(bookMarked = false)
                            _event.emit(BookmarkFailure("북마크는 12개까지 가능해요"))
                        }
                        if (it.isNetworkError()) {
                            _uiState.value = uiState.copy(bookMarked = false)
                            _event.emit(BookmarkFailure("인터넷 연결을 확인해주세요"))
                        }
                    }
            }
        }
    }

    private fun handleFailure(key: String, throwable: Throwable) {
        _uiState.value = ArtistDetailUiState.Error {
            _uiState.value = ArtistDetailUiState.Loading
            loadArtistDetail(it, 0L)
        }
        analyticsHelper.logNetworkFailure(
            key = key,
            value = throwable.message.toString(),
        )
    }

    private fun FestivalsPage.toUiState() = festivals.map {
        FestivalItemUiState(
            id = it.id,
            name = it.name,
            imageUrl = it.imageUrl,
            startDate = it.startDate,
            endDate = it.endDate,
            artists = it.artists.map { artist ->
                ArtistUiState(
                    id = artist.id,
                    name = artist.name,
                    imageUrl = artist.imageUrl,
                    onArtistDetailClick = { artistDetail ->
                        viewModelScope.launch {
                            _event.emit(ShowArtistDetail(artistDetail))
                        }
                    },
                )
            },
            onFestivalDetailClick = { festivalDetail ->
                viewModelScope.launch {
                    _event.emit(ShowFestivalDetail(festivalDetail))
                }
            },
        )
    }

    companion object {
        private const val FESTIVAL_PAGE_SIZE = 20
        private const val KEY_LOAD_ARTIST_DETAIL = "KEY_LOAD_ARTIST_DETAIL"
        private const val KEY_LOAD_MORE_ARTIST_FESTIVAL = "KEY_LOAD_MORE_ARTIST_FESTIVAL"
    }
}
