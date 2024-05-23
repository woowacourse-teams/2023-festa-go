package com.festago.stage.repository;

import com.festago.stage.domain.Stage;
import java.util.List;
import java.util.Optional;

public interface StageRepositoryCustom {

    List<Stage> findAllDetailByFestivalId(Long festivalId);

    Optional<Stage> findByIdWithFetch(Long id);
}
