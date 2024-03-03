package com.festago.stage.domain;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.FestivalIdStageArtistsResolver;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class FestivalIdStageArtistsResolverImpl implements FestivalIdStageArtistsResolver {

    private final StageRepository stageRepository;
    private final StageArtistRepository stageArtistRepository;
    private final ArtistRepository artistRepository;

    @Override
    public List<Artist> resolve(Long festivalId) {
        List<Long> stageIds = stageRepository.findAllByFestivalId(festivalId).stream()
            .map(Stage::getId)
            .toList();
        Set<Long> artistIds = stageArtistRepository.findAllArtistIdByStageIdIn(stageIds);
        List<Artist> artists = artistRepository.findByIdIn(artistIds);
        if (artists.size() != artistIds.size()) {
            throw new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND);
        }
        return artists;
    }
}
