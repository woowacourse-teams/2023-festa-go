package com.festago.festago.presentation.ui.artistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festago.festago.domain.model.artist.Stages
import com.festago.festago.domain.repository.ArtistRepository
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistDetailUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.StageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ArtistDetailUiState> =
        MutableStateFlow(ArtistDetailUiState.Loading)
    val uiState: StateFlow<ArtistDetailUiState> = _uiState.asStateFlow()

    fun loadArtistDetail(id: Long) {
        viewModelScope.launch {
            runCatching {
                _uiState.value = ArtistDetailUiState.Success(
                    artistRepository.loadArtistDetail(id).getOrThrow(),
                    artistRepository.loadArtistStages(id, 20).getOrThrow().toUiState(),
                )
            }.onFailure {
                _uiState.value = ArtistDetailUiState.Error
            }
        }
    }

    private fun Stages.toUiState() = this.stage.map {
        StageUiState(
            id = it.id,
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
