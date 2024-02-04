package com.festago.festago.presentation.ui.artistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.model.artist.ArtistDetail
import com.festago.festago.domain.model.artist.Stages
import com.festago.festago.domain.repository.ArtistRepository
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistDetailUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.StageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    artistRepository: ArtistRepository
) : ViewModel() {
    val uiState: StateFlow<ArtistDetailUiState> =
        combine<ArtistDetail, Stages, ArtistDetailUiState>(
            flow { emit(artistRepository.loadArtistDetail().getOrThrow()) },
            flow { emit(artistRepository.loadArtistStages(20).getOrThrow()) }
        ) { artist, stages ->
            ArtistDetailUiState.Success(artist, stages.toUiState())
        }
            .catch { emit(ArtistDetailUiState.Error) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ArtistDetailUiState.Loading
            )

    private fun Stages.toUiState() = this.stage.map {
        StageUiState(
            id = it.id.toLong(),
            name = it.name,
            imageUrl = it.imageUrl,
            startDate = it.startDate,
            endDate = it.endDate,
            artists = it.artists.map { artist ->
                ArtistUiState(artist.id, artist.name, artist.imageUrl)
            },
        )
    }
}

