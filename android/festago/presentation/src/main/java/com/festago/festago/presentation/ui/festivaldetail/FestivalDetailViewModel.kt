package com.festago.festago.presentation.ui.festivaldetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.logNetworkFailure
import com.festago.festago.domain.model.artist.Artist
import com.festago.festago.domain.model.festival.FestivalDetail
import com.festago.festago.domain.model.stage.Stage
import com.festago.festago.domain.repository.BookmarkRepository
import com.festago.festago.domain.repository.FestivalRepository
import com.festago.festago.presentation.ui.festivaldetail.uiState.ArtistItemUiState
import com.festago.festago.presentation.ui.festivaldetail.uiState.FestivalDetailUiState
import com.festago.festago.presentation.ui.festivaldetail.uiState.FestivalUiState
import com.festago.festago.presentation.ui.festivaldetail.uiState.StageItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FestivalDetailUiState>(FestivalDetailUiState.Loading)
    val uiState: StateFlow<FestivalDetailUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<FestivalDetailEvent>()
    val event: SharedFlow<FestivalDetailEvent> = _event.asSharedFlow()

    fun loadFestivalDetail(festivalId: Long, refresh: Boolean = false) {
        if (!refresh && _uiState.value is FestivalDetailUiState.Success) return

        viewModelScope.launch {
            val deferredFestivalDetail = festivalRepository.loadFestivalDetail(festivalId)
            val deferredBookmarks = bookmarkRepository.getFestivalBookmarkIds()

            runCatching {
                val festivalDetail = deferredFestivalDetail.getOrThrow()
                val festivalBookmarkIds = deferredBookmarks.getOrThrow()

                _uiState.value = festivalDetail.toSuccessUiState()
                    .copy(bookmarked = festivalBookmarkIds.contains(festivalId))
            }.onFailure {
                _uiState.value = FestivalDetailUiState.Error
                analyticsHelper.logNetworkFailure(
                    key = KEY_LOAD_FESTIVAL_DETAIL,
                    value = it.message.toString(),
                )
            }


        }
    }

    private fun FestivalDetail.toSuccessUiState() =
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
            bookmarked = false,
            stages = stages.map { it.toUiState() },
            onBookmarkClick = { festivalId -> toggleFestivalBookmark(festivalId) },
        )

    private fun toggleFestivalBookmark(festivalId: Long) {
        viewModelScope.launch {
            val uiState = _uiState.value as? FestivalDetailUiState.Success ?: return@launch

            if (uiState.bookmarked) {
                bookmarkRepository.deleteFestivalBookmark(festivalId)
                    .onSuccess { _uiState.value = uiState.copy(bookmarked = false) }
            } else {
                bookmarkRepository.addFestivalBookmark(festivalId)
                    .onSuccess { _uiState.value = uiState.copy(bookmarked = true) }
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

    private fun showArtistDetail(artistId: Long) {
        viewModelScope.launch {
            _event.emit(FestivalDetailEvent.ShowArtistDetail(artistId))
        }
    }

    private fun showSchoolDetail(schoolId: Long) {
        viewModelScope.launch {
            _event.emit(FestivalDetailEvent.ShowSchoolDetail(schoolId))
        }
    }

    companion object {
        private const val KEY_LOAD_FESTIVAL_DETAIL = "KEY_LOAD_FESTIVAL_DETAIL"
    }
}
