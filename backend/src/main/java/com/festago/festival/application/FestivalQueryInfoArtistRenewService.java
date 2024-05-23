package com.festago.festival.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.FestivalIdStageArtistsResolver;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.repository.FestivalInfoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalQueryInfoArtistRenewService {

    private final FestivalInfoRepository festivalInfoRepository;
    private final FestivalIdStageArtistsResolver festivalIdStageArtistsResolver;
    private final ArtistsSerializer serializer;

    public void renewArtistInfo(Long festivalId) {
        FestivalQueryInfo festivalQueryInfo = festivalInfoRepository.findByFestivalId(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
        List<Artist> artists = festivalIdStageArtistsResolver.resolve(festivalId);
        festivalQueryInfo.updateArtistInfo(artists, serializer);
    }
}
