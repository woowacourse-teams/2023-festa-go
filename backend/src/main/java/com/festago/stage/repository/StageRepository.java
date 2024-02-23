package com.festago.stage.repository;

import com.festago.stage.domain.Stage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface StageRepository extends Repository<Stage, Long>, StageRepositoryCustom {

    Stage save(Stage stage);

    Optional<Stage> findById(Long stageId);

    void deleteById(Long stageId);

    void flush();

    boolean existsByFestivalId(Long festivalId);

    List<Stage> findAllByFestivalId(Long festivalId);

}
