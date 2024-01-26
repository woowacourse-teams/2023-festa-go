package com.festago.festival.repository;

import com.festago.festival.domain.FestivalQueryInfo;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface FestivalInfoRepository extends Repository<FestivalQueryInfo, Long> {

    FestivalQueryInfo save(FestivalQueryInfo festivalQueryInfo);

    List<FestivalQueryInfo> findAllByFestivalIdIn(List<Long> festivalIds);
}

