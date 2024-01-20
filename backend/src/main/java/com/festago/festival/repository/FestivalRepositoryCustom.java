package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import java.time.LocalDate;
import java.util.List;

public interface FestivalRepositoryCustom {

    List<Festival> findByFilter(FestivalFilter filter, LocalDate currentTime);

    FestivalPage findBy(FestivalFilter filter, FestivalPageable page, LocalDate currentTime);

    FestivalPage findBy(FestivalFilter filter, List<School> schools, FestivalPageable page, LocalDate currentTime);
}
