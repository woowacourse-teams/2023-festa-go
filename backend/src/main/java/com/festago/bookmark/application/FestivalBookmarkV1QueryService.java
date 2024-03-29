package com.festago.bookmark.application;

import com.festago.bookmark.dto.v1.FestivalBookmarkV1Response;
import com.festago.bookmark.repository.FestivalBookmarkOrder;
import com.festago.bookmark.repository.FestivalBookmarkV1QueryDslRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FestivalBookmarkV1QueryService {

    private final FestivalBookmarkV1QueryDslRepository festivalBookmarkV1QueryDslRepository;

    public List<Long> findBookmarkedFestivalIds(Long memberId) {
        return festivalBookmarkV1QueryDslRepository.findBookmarkedFestivalIds(memberId);
    }

    public List<FestivalBookmarkV1Response> findBookmarkedFestivals(
        Long memberId,
        List<Long> festivalIds,
        FestivalBookmarkOrder festivalBookmarkOrder
    ) {
        return festivalBookmarkV1QueryDslRepository.findBookmarkedFestivals(
            memberId,
            festivalIds,
            festivalBookmarkOrder
        );
    }
}
