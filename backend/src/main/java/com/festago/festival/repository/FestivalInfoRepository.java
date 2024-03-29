package com.festago.festival.repository;

import com.festago.festival.domain.FestivalQueryInfo;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface FestivalInfoRepository extends Repository<FestivalQueryInfo, Long> {

    FestivalQueryInfo save(FestivalQueryInfo festivalQueryInfo);

    Optional<FestivalQueryInfo> findByFestivalId(Long festivalId);

    void deleteByFestivalId(Long festivalId);
}

