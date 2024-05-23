package com.festago.stage.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.common.exception.NotFoundException;
import com.festago.stage.domain.StageQueryInfo;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageQueryInfoRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StageQueryInfoService {

    private final StageQueryInfoRepository stageQueryInfoRepository;
    private final StageArtistRepository stageArtistRepository;
    private final ArtistRepository artistRepository;
    private final ArtistsSerializer serializer;

    public void initialStageQueryInfo(Long stageId) {
        List<Artist> artists = getStageArtists(stageId);
        StageQueryInfo stageQueryInfo = StageQueryInfo.of(stageId, artists, serializer);
        stageQueryInfoRepository.save(stageQueryInfo);
    }

    private List<Artist> getStageArtists(Long stageId) {
        Set<Long> artistIds = stageArtistRepository.findAllArtistIdByStageId(stageId);
        List<Artist> artists = artistRepository.findByIdIn(artistIds);
        if (artists.size() != artistIds.size()) {
            log.error("StageArtist에 존재하지 않은 Artist가 있습니다. artistsIds=" + artistIds);
            throw new InternalServerException(ErrorCode.ARTIST_NOT_FOUND);
        }
        return artists;
    }

    public void renewalStageQueryInfo(Long stageId) {
        StageQueryInfo stageQueryInfo = stageQueryInfoRepository.findByStageId(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
        List<Artist> artists = getStageArtists(stageId);
        stageQueryInfo.updateArtist(artists, serializer);
    }

    public void deleteStageQueryInfo(Long stageId) {
        stageQueryInfoRepository.deleteByStageId(stageId);
    }
}
