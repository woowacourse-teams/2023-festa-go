package com.festago.mock.repository;

import com.festago.stage.domain.StageArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForMockStageArtistRepository extends JpaRepository<StageArtist, Long> {

}
