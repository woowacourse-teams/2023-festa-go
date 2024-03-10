package com.festago.artist.application;

import com.festago.artist.dto.ArtistSearchResponse;
import com.festago.artist.dto.ArtistSearchStageCount;
import com.festago.artist.dto.ArtistSearchTotalResponse;
import com.festago.artist.repository.ArtistV1SearchQueryDslRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistV1SearchQueryService {

    private static final int MAX_SEARCH_COUNT = 10;

    private final ArtistV1SearchQueryDslRepository artistV1SearchQueryDslRepository;

    public List<ArtistSearchTotalResponse> findAllByKeyword(String keyword, LocalDate today) {
        List<ArtistSearchResponse> artists = findByKeywordLength(keyword);
        validateSearchCount(artists);
        List<Long> artistIds = artists.stream()
            .map(ArtistSearchResponse::id)
            .toList();
        Map<Long, ArtistSearchStageCount> artistToStageCount = artistV1SearchQueryDslRepository.findArtistsStageScheduleAfterDate(
            artistIds, today);
        return artists.stream()
            .map(it -> ArtistSearchTotalResponse.of(it, artistToStageCount.get(it.id())))
            .toList();
    }

    private List<ArtistSearchResponse> findByKeywordLength(String keyword) {
        if (keyword.length() == 1) {
            return artistV1SearchQueryDslRepository.findAllByEqual(keyword);
        }
        return artistV1SearchQueryDslRepository.findAllByLike(keyword);
    }

    private void validateSearchCount(List<ArtistSearchResponse> response) {
        if (response.size() >= MAX_SEARCH_COUNT) {
            throw new BadRequestException(ErrorCode.BROAD_SEARCH_KEYWORD);
        }
    }
}
