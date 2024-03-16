package com.festago.festival.application;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.dto.FestivalDetailV1Response;
import com.festago.festival.repository.FestivalDetailV1QueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FestivalDetailV1QueryService {

    private final FestivalDetailV1QueryDslRepository festivalDetailV1QueryDslRepository;

    public FestivalDetailV1Response findFestivalDetail(Long festivalId) {
        return festivalDetailV1QueryDslRepository.findFestivalDetail(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
    }
}
