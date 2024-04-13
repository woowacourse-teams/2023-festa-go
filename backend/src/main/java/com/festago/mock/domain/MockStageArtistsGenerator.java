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

    public List<StageArtist> generate(int stagePerArtist, List<Stage> stages, List<Artist> artists) {
        if (stages.size() > artists.size()) {
            throw new UnexpectedException("공연의 개수는 아티스트의 개수를 초과할 수 없습니다.");
        }
        Queue<Artist> artistQueue = createShuffledArtistQueue(new ArrayList<>(artists));
        List<StageArtist> stageArtists = new ArrayList<>();
        for (int i = 0; i < stagePerArtist; i++) {
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
