package com.festago.festival.repository;

import com.festago.festival.domain.FestivalQueryInfo;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface FestivalQueryInfoRepository extends Repository<FestivalQueryInfo, Long> {

    FestivalQueryInfo save(FestivalQueryInfo festivalQueryInfo);

    Optional<FestivalQueryInfo> findByFestivalId();
}

