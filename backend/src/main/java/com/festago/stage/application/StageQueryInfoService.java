package com.festago.stage.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageQueryInfo;
import com.festago.stage.repository.StageQueryInfoRepository;
import java.util.List;
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
    private final ArtistRepository artistRepository;
    private final ArtistsSerializer serializer;

    public void initialStageQueryInfo(Stage stage) {
        List<Artist> artists = artistRepository.findByIdIn(stage.getArtistIds());
        StageQueryInfo stageQueryInfo = StageQueryInfo.of(stage.getId(), artists, serializer);
        stageQueryInfoRepository.save(stageQueryInfo);
    }

    public void renewalStageQueryInfo(Stage stage) {
        StageQueryInfo stageQueryInfo = stageQueryInfoRepository.findByStageId(stage.getId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
        List<Artist> artists = artistRepository.findByIdIn(stage.getArtistIds());
        stageQueryInfo.updateArtist(artists, serializer);
    }

    public void deleteStageQueryInfo(Long stageId) {
        stageQueryInfoRepository.deleteByStageId(stageId);
    }
}
