package com.festago.stage.repository;

import com.festago.stage.domain.StageArtist;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface StageArtistRepository extends Repository<StageArtist, Long> {

    StageArtist save(StageArtist stageArtist);

    List<StageArtist> findAll();
}
