package com.festago.application;

import com.festago.domain.Festival;
import com.festago.domain.FestivalRepository;
import com.festago.dto.FestivalCreateRequest;
import com.festago.dto.FestivalDetailResponse;
import com.festago.dto.FestivalResponse;
import com.festago.dto.FestivalsResponse;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FestivalService {

    private final FestivalRepository festivalRepository;

    public FestivalService(FestivalRepository festivalRepository) {
        this.festivalRepository = festivalRepository;
    }

    public FestivalResponse create(FestivalCreateRequest request) {
        Festival newFestival = festivalRepository.save(request.toEntity());
        return FestivalResponse.from(newFestival);
    }

    @Transactional(readOnly = true)
    public FestivalsResponse findAll() {
        List<Festival> festivals = festivalRepository.findAll();
        return FestivalsResponse.from(festivals);
    }

    @Transactional(readOnly = true)
    public FestivalDetailResponse findDetail(Long festivalId) {
        Festival festival = festivalRepository.findById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
        return FestivalDetailResponse.from(festival);
    }
}
