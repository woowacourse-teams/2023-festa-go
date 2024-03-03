package com.festago.artist.application;

import com.festago.artist.dto.ArtistSearchResponse;
import com.festago.artist.repository.ArtistV1SearchQueryDslRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistV1SearchQueryService {

    private static final int MAX_SEARCH_COUNT = 10;

    private final ArtistV1SearchQueryDslRepository artistV1SearchQueryDslRepository;

    public List<ArtistSearchResponse> findAllByKeyword(String keyword) {
        List<ArtistSearchResponse> response = findByKeywordLength(keyword);
        if (response.size() >= MAX_SEARCH_COUNT) {
            throw new BadRequestException(ErrorCode.BROAD_SEARCH_KEYWORD);
        }
        return response;
    }

    private List<ArtistSearchResponse> findByKeywordLength(String keyword) {
        if (keyword.length() == 1) {
            return artistV1SearchQueryDslRepository.findAllByEqual(keyword);
        }
        return artistV1SearchQueryDslRepository.findAllByLike(keyword);
    }
}
