package com.festago.festago.presentation.ui.schooldetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.domain.exception.isBookmarkLimitExceeded
import com.festago.festago.domain.exception.isNetworkError
import com.festago.festago.domain.exception.isUnauthorized
import com.festago.festago.domain.model.bookmark.BookmarkType
import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.repository.BookmarkRepository
import com.festago.festago.domain.repository.SchoolRepository
import com.festago.festago.domain.repository.UserRepository
import com.festago.festago.presentation.ui.schooldetail.SchoolDetailEvent.BookmarkFailure
import com.festago.festago.presentation.ui.schooldetail.SchoolDetailEvent.BookmarkSuccess
import com.festago.festago.presentation.ui.schooldetail.SchoolDetailEvent.ShowArtistDetail
import com.festago.festago.presentation.ui.schooldetail.SchoolDetailEvent.ShowFestivalDetail
import com.festago.festago.presentation.ui.schooldetail.uistate.ArtistUiState
import com.festago.festago.presentation.ui.schooldetail.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.schooldetail.uistate.SchoolDetailUiState
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
class SchoolDetailViewModel @Inject constructor(
    private val schoolRepository: SchoolRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val userRepository: UserRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SchoolDetailUiState>(SchoolDetailUiState.Loading)
    val uiState: StateFlow<SchoolDetailUiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<SchoolDetailEvent> = MutableSharedFlow()
    val event: SharedFlow<SchoolDetailEvent> = _event.asSharedFlow()

    fun loadSchoolDetail(schoolId: Long, delayTimeMillis: Long) {
        viewModelScope.launch {
            val deferredSchoolInfo =
                async { schoolRepository.loadSchoolInfo(schoolId, delayTimeMillis) }
            val deferredFestivalPage = async {
                schoolRepository.loadSchoolFestivals(schoolId = schoolId, size = FESTIVAL_PAGE_SIZE)
            }

            runCatching {
                val schoolInfo = deferredSchoolInfo.await().getOrThrow()
                val festivalPage = deferredFestivalPage.await().getOrThrow()

                val isBookmarked = if (userRepository.isSigned()) {
                    bookmarkRepository.isBookmarked(schoolId, BookmarkType.SCHOOL)
                } else {
                    false
                }

                _uiState.value = SchoolDetailUiState.Success(
                    schoolInfo = schoolInfo,
                    bookmarked = isBookmarked,
                    festivals = festivalPage.festivals.map { it.toUiState() },
                    isLast = festivalPage.isLastPage,
                    onBookmarkClick = { schoolId -> toggleSchoolBookmark(schoolId) },
                )
                if (!festivalPage.isLastPage) {
                    return@launch
                }
                val lastFestival = festivalPage.festivals.lastOrNull()
                if (lastFestival == null || lastFestival.endDate >= LocalDate.now()) {
                    loadMoreSchoolFestivals(schoolId)
                    return@launch
                }
            }.onFailure {
                handleFailure(key = KEY_LOAD_SCHOOL_DETAIL, throwable = it)
            }
        }
    }

    fun loadMoreSchoolFestivals(schoolId: Long) {
        val successUiState = uiState.value as? SchoolDetailUiState.Success ?: return

        viewModelScope.launch {
            val currentFestivals = successUiState.festivals
            val lastItem = currentFestivals.lastOrNull()
            val isPast =
                lastItem?.endDate?.isBefore(LocalDate.now()) ?: true || successUiState.isLast
            val lastFestivalId = if (successUiState.isLast) null else lastItem?.id
            val lastStartDate = if (successUiState.isLast) null else lastItem?.startDate
            schoolRepository.loadSchoolFestivals(
                schoolId = schoolId,
                lastFestivalId = lastFestivalId?.toInt(),
                lastStartDate = lastStartDate,
                isPast = isPast,
                size = FESTIVAL_PAGE_SIZE,
            ).onSuccess { festivalsPage ->
                _uiState.value = successUiState.copy(
                    festivals = currentFestivals + festivalsPage.festivals.map { it.toUiState() },
                    isLast = festivalsPage.isLastPage,
                )
            }.onFailure {
                handleFailure(key = KEY_LOAD_MORE_SCHOOL_FESTIVALS, throwable = it)
            }
        }
    }

    private fun toggleSchoolBookmark(schoolId: Int) {
        val uiState = uiState.value as? SchoolDetailUiState.Success ?: return

        viewModelScope.launch {
            if (uiState.bookmarked) {
                _uiState.value = uiState.copy(bookmarked = false)
                bookmarkRepository.deleteSchoolBookmark(schoolId.toLong())
                    .onSuccess { _event.emit(BookmarkSuccess(false)) }
                    .onFailure {
                        if (it.isUnauthorized()) {
                            _event.emit(BookmarkFailure("로그인이 필요해요"))
                        }
                        if (it.isNetworkError()) {
                            _uiState.value = uiState.copy(bookmarked = true)
                            _event.emit(BookmarkFailure("인터넷 연결을 확인해주세요"))
                        }
                    }
            } else {
                _uiState.value = uiState.copy(bookmarked = true)
                bookmarkRepository.addSchoolBookmark(uiState.schoolInfo.id.toLong())
                    .onSuccess { _event.emit(BookmarkSuccess(true)) }
                    .onFailure {
                        if (it.isUnauthorized()) {
                            _event.emit(BookmarkFailure("로그인이 필요해요"))
                        }
                        if (it.isBookmarkLimitExceeded()) {
                            _uiState.value = uiState.copy(bookmarked = false)
                            _event.emit(BookmarkFailure("북마크는 12개까지 가능해요"))
                        }
                        if (it.isNetworkError()) {
                            _uiState.value = uiState.copy(bookmarked = false)
                            _event.emit(BookmarkFailure("인터넷 연결을 확인해주세요"))
                        }
                    }
            }
        }
    }

    private fun handleFailure(key: String, throwable: Throwable) {
        _uiState.value = SchoolDetailUiState.Error {
            _uiState.value = SchoolDetailUiState.Loading
            loadSchoolDetail(it, 0L)
        }
        analyticsHelper.logNetworkFailure(
            key = key,
            value = throwable.message.toString(),
        )
    }

    private fun Festival.toUiState() = FestivalItemUiState(
        id = id,
        name = name,
        startDate = startDate,
        endDate = endDate,
        imageUrl = imageUrl,
        artists = artists.map { artist ->
            ArtistUiState(
                id = artist.id,
                name = artist.name,
                imageUrl = artist.imageUrl,
                onArtistDetailClick = { artistUiState ->
                    viewModelScope.launch {
                        _event.emit(ShowArtistDetail(artistUiState))
                    }
                },
            )
        },
        onFestivalDetailClick = { festivalUiState ->
            viewModelScope.launch {
                _event.emit(ShowFestivalDetail(festivalUiState))
            }
        },
    )

    companion object {
        private const val FESTIVAL_PAGE_SIZE = 20
        private const val KEY_LOAD_SCHOOL_DETAIL = "KEY_LOAD_SCHOOL_DETAIL"
        private const val KEY_LOAD_MORE_SCHOOL_FESTIVALS = "KEY_LOAD_MORE_SCHOOL_FESTIVALS"
    }
}
