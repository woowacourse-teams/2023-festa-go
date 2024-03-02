package com.festago.artist.application;

import com.festago.artist.dto.ArtistSearchResponse;
import com.festago.artist.repository.ArtistV1SearchQueryDslRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistV1SearchQueryService {

    private final ArtistV1SearchQueryDslRepository artistV1SearchQueryDslRepository;

    public List<ArtistSearchResponse> findAllByKeyword(String keyword) {
        List<ArtistSearchResponse> response = findByKeywordLength(keyword);
        if (response.size() >= 10) {
            throw new IllegalStateException("더 자세한 키워드로 검색해주세요");
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
