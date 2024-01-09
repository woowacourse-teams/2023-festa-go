package com.festago.festival.application;

import com.festago.festival.dto.DetailFestivalResponse;

public interface FestivalStageService {

    DetailFestivalResponse findDetail(Long festivalId);
}
