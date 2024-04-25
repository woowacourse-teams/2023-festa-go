package com.festago.mock.domain;

import com.festago.artist.domain.Artist;
import com.festago.common.exception.UnexpectedException;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import org.springframework.stereotype.Component;

@Component
public class MockStageArtistsGenerator {

    private static final int STAGE_PER_ARTIST = 3;

    /**
     * 영속되지 않은 상태의 StageArtist 목록을 생성합니다. <br/> StageArtist는 Stage와 Artist의 식별자가 필요하므로, 인자로 들어오는 Stage, Artist는 영속된 상태여야
     * 합니다. <br/> 공연에 STAGE_PER_ARTIST 만큼 아티스트를 참여시키는게 불가능할 경우, 각 공연 별 최소 1명은 참가하는 것을 보장합니다. <br/> 따라서, stages.size() >
     * artists.size() 이면 예외가 발생합니다. <br/> 생성된 StageArtist 목록에는 중복된 Artist가 존재하지 않습니다.<br/>
     *
     * @param stages  영속 상태의 아티스트가 참여될 공연 목록
     * @param artists 영속 상태의 공연에 참여할 아티스트 목록
     * @return 중복된 Artist가 없는 영속되지 않은 StageArtist 엔티티 리스트
     * @throws UnexpectedException 공연의 개수가 아티스트의 개수를 초과하면
     */
    public List<StageArtist> generate(List<Stage> stages, List<Artist> artists) {
        if (stages.size() > artists.size()) {
            throw new UnexpectedException("공연의 개수는 아티스트의 개수를 초과할 수 없습니다.");
        }
        Queue<Artist> artistQueue = createShuffledArtistQueue(new ArrayList<>(artists));
        List<StageArtist> stageArtists = new ArrayList<>();
        for (int i = 0; i < STAGE_PER_ARTIST; i++) {
            appendStageArtist(stageArtists, stages, artistQueue);
        }
        return stageArtists;
    }

    private Queue<Artist> createShuffledArtistQueue(List<Artist> artists) {
        Collections.shuffle(artists);
        return new ArrayDeque<>(artists);
    }

    private void appendStageArtist(
        List<StageArtist> stageArtists,
        List<Stage> stages,
        Queue<Artist> artistQueue
    ) {
        for (Stage stage : stages) {
            if (artistQueue.isEmpty()) {
                return;
            }
            Artist artist = artistQueue.poll();
            stageArtists.add(new StageArtist(stage.getId(), artist.getId()));
        }
    }
}
