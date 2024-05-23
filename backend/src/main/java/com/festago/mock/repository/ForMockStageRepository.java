package com.festago.mock.repository;

import com.festago.stage.domain.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForMockStageRepository extends JpaRepository<Stage, Long> {

}
