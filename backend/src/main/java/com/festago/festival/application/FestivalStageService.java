package com.festago.festival.application;

import com.festago.festival.dto.DetailFestivalResponse;
import org.springframework.stereotype.Component;

@Component
public interface FestivalStageService {

    DetailFestivalResponse findDetail(Long festivalId);
}
