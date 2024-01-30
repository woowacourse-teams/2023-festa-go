package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import java.time.LocalDate;
import java.util.List;

public interface FestivalRepositoryCustom {

    List<Festival> findByFilter(FestivalFilter filter, LocalDate currentTime);
}
