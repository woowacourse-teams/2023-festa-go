package com.festago.festago.presentation.ui.festivaldetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.domain.model.artist.Artist
import com.festago.festago.domain.model.bookmark.BookmarkType
import com.festago.festago.domain.model.festival.FestivalDetail
import com.festago.festago.domain.model.school.School
import com.festago.festago.domain.model.stage.Stage
import com.festago.festago.domain.repository.BookmarkRepository
import com.festago.festago.domain.repository.FestivalRepository
import com.festago.festago.domain.repository.UserRepository
import com.festago.festago.presentation.ui.festivaldetail.FestivalDetailEvent.BookmarkFailure
import com.festago.festago.presentation.ui.festivaldetail.FestivalDetailEvent.BookmarkSuccess
import com.festago.festago.presentation.ui.festivaldetail.FestivalDetailEvent.ShowArtistDetail
import com.festago.festago.presentation.ui.festivaldetail.FestivalDetailEvent.ShowSchoolDetail
import com.festago.festago.presentation.ui.festivaldetail.uiState.ArtistItemUiState
import com.festago.festago.presentation.ui.festivaldetail.uiState.FestivalDetailUiState
import com.festago.festago.presentation.ui.festivaldetail.uiState.FestivalUiState
import com.festago.festago.presentation.ui.festivaldetail.uiState.StageItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FestivalDetailViewModel @Inject constructor(
    private val festivalRepository: FestivalRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val userRepository: UserRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FestivalDetailUiState>(FestivalDetailUiState.Loading)
    val uiState: StateFlow<FestivalDetailUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<FestivalDetailEvent>()
    val event: SharedFlow<FestivalDetailEvent> = _event.asSharedFlow()

    fun loadFestivalDetail(festivalId: Long, delayTimeMillis: Long, refresh: Boolean = false) {
        if (!refresh && _uiState.value is FestivalDetailUiState.Success) return

        viewModelScope.launch {
            val deferredFestivalDetail =
                async { festivalRepository.loadFestivalDetail(festivalId, delayTimeMillis) }

            runCatching {
                val festivalDetail = deferredFestivalDetail.await().getOrThrow()

                val isBookmarked = if (userRepository.isSigned()) {
                    bookmarkRepository.isBookmarked(festivalId, BookmarkType.FESTIVAL)
                } else {
                    false
                }

                _uiState.value = festivalDetail.toSuccessUiState(isBookmarked)
            }.onFailure {
                _uiState.value = FestivalDetailUiState.Error { festivalId ->
                    _uiState.value = FestivalDetailUiState.Loading
                    loadFestivalDetail(festivalId, 0L)
                }
                analyticsHelper.logNetworkFailure(
                    key = KEY_LOAD_FESTIVAL_DETAIL,
                    value = it.message.toString(),
                )
            }
        }
    }

    private fun FestivalDetail.toSuccessUiState(isBookmarked: Boolean) =
        FestivalDetailUiState.Success(
            festival = FestivalUiState(
                id = id,
                name = name,
                startDate = startDate,
                endDate = endDate,
                posterImageUrl = posterImageUrl,
                school = school,
                onSchoolClick = ::showSchoolDetail,
                socialMedias = socialMedias,
            ),
            bookmarked = isBookmarked,
            stages = stages.map { it.toUiState() },
            onBookmarkClick = { festivalId -> toggleFestivalBookmark(festivalId) },
        )

    private fun toggleFestivalBookmark(festivalId: Long) {
        viewModelScope.launch {
            val uiState = _uiState.value as? FestivalDetailUiState.Success ?: return@launch

            if (!userRepository.isSigned()) {
                _event.emit(BookmarkFailure("로그인이 필요합니다"))
                return@launch
            }

            if (uiState.bookmarked) {
                _uiState.value = uiState.copy(bookmarked = false)
                bookmarkRepository.deleteFestivalBookmark(festivalId)
                    .onSuccess { _event.emit(BookmarkSuccess(false)) }
                    .onFailure {
                        _uiState.value = uiState.copy(bookmarked = true)
                        _event.emit(BookmarkFailure("북마크를 해제할 수 없습니다. 인터넷 연결을 확인해주세요"))
                    }
            } else {
                _uiState.value = uiState.copy(bookmarked = true)
                bookmarkRepository.addFestivalBookmark(festivalId)
                    .onSuccess { _event.emit(BookmarkSuccess(true)) }
                    .onFailure {
                        _uiState.value = uiState.copy(bookmarked = false)
                        _event.emit(BookmarkFailure("다른 북마크를 해제하거나 인터넷 연결을 확인해주세요"))
                    }
            }
        }
    }

    private fun Stage.toUiState() = StageItemUiState(
        id = id,
        startDateTime = startDateTime,
        artists = artists.map { it.toUiState() },
    )

    private fun Artist.toUiState() = ArtistItemUiState(
        id = id,
        name = name,
        imageUrl = imageUrl,
        onArtistDetail = ::showArtistDetail,
    )

    private fun showArtistDetail(artist: ArtistItemUiState) {
        viewModelScope.launch {
            _event.emit(ShowArtistDetail(artist))
        }
    }

    private fun showSchoolDetail(school: School) {
        viewModelScope.launch {
            _event.emit(ShowSchoolDetail(school))
        }
    }

    companion object {
        private const val KEY_LOAD_FESTIVAL_DETAIL = "KEY_LOAD_FESTIVAL_DETAIL"
    }
}
