package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import com.festago.festival.dto.FestivalV1Response;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Slice;

public interface FestivalRepositoryCustom {

    List<Festival> findByFilter(FestivalFilter filter, LocalDate currentTime);

    Slice<FestivalV1Response> findBy(FestivalSearchCondition hello);
}
