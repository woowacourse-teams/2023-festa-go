package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalInfo;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface FestivalInfoRepository extends Repository<FestivalInfo, Long> {

    List<FestivalInfo> findAllByFestivalIn(List<Festival> festivals);
}
