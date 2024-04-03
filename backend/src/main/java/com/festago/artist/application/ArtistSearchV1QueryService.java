package com.festago.artist.application;

import com.festago.artist.dto.ArtistSearchV1Response;
import com.festago.artist.repository.ArtistSearchV1QueryDslRepository;
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

    private final ArtistSearchV1QueryDslRepository artistSearchV1QueryDslRepository;

    public List<ArtistSearchV1Response> findAllByKeyword(String keyword) {
        List<ArtistSearchV1Response> response = getResponse(keyword);
        if (response.size() >= MAX_SEARCH_COUNT) {
            throw new BadRequestException(ErrorCode.BROAD_SEARCH_KEYWORD);
        }
        return response;
    }

    private List<ArtistSearchV1Response> getResponse(String keyword) {
        if (keyword.length() == 1) {
            return artistSearchV1QueryDslRepository.findAllByEqual(keyword);
        }
        return artistSearchV1QueryDslRepository.findAllByLike(keyword);
    }
}
