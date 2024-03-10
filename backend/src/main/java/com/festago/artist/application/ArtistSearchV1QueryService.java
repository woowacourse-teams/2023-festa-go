package com.festago.artist.application;

import com.festago.artist.dto.ArtistSearchV1Response;
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
public class ArtistSearchV1QueryService {

    private static final int MAX_SEARCH_COUNT = 10;

    private final ArtistV1SearchQueryDslRepository artistV1SearchQueryDslRepository;

    public List<ArtistSearchV1Response> findAllByKeyword(String keyword) {
        List<ArtistSearchV1Response> response = getResponse(keyword);
        if (response.size() >= MAX_SEARCH_COUNT) {
            throw new BadRequestException(ErrorCode.BROAD_SEARCH_KEYWORD);
        }
        return response;
    }

    private List<ArtistSearchV1Response> getResponse(String keyword) {
        if (keyword.length() == 1) {
            return artistV1SearchQueryDslRepository.findAllByEqual(keyword);
        }
        return artistV1SearchQueryDslRepository.findAllByLike(keyword);
    }
}
