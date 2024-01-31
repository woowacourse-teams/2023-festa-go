package com.festago.stage.repository;

import com.festago.stage.domain.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<Stage, Long>, StageRepositoryCustom {

    boolean existsByFestivalId(Long festivalId);
}
