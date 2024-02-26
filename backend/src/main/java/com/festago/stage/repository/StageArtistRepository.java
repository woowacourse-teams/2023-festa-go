package com.festago.stage.repository;

import com.festago.stage.domain.StageArtist;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface StageArtistRepository extends Repository<StageArtist, Long> {

    StageArtist save(StageArtist stageArtist);

    // TODO Custom 클래스를 새롭게 만들어서 JPQL을 작성할 필요가 있을까?
    @Query("select sa.artistId from StageArtist sa where sa.stageId = :stageId")
    Set<Long> findAllArtistIdByStageId(@Param("stageId") Long stageId);

    @Query("select sa.artistId from StageArtist sa where sa.stageId in :stageIds")
    Set<Long> findAllArtistIdByStageIdIn(@Param("stageIds") List<Long> stageIds);

    void deleteByStageId(Long stageId);
}
