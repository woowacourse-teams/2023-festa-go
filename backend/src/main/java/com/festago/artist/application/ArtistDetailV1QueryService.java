package com.festago.artist.application;

import com.festago.artist.dto.ArtistDetailV1Response;
import com.festago.artist.dto.ArtistFestivalDetailV1Response;
import com.festago.artist.presentation.v1.ArtistFestivalDetailV1Request;
import com.festago.artist.repository.ArtistFestivalSearchCondition;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import java.time.Clock;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistDetailV1QueryService {

    private final ArtistDetailV1QueryDslRepository artistDetailV1QueryDslRepository;
    private final Clock clock;

    public ArtistDetailV1Response findArtistDetail(Long artistId) {
        return artistDetailV1QueryDslRepository.findArtistDetail(artistId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ARTIST_NOT_FOUND));
    }

    public Slice<ArtistFestivalDetailV1Response> findArtistFestivals(
        Long artistId,
        ArtistFestivalDetailV1Request request,
        Pageable pageable) {
        return artistDetailV1QueryDslRepository.findArtistFestivals(new ArtistFestivalSearchCondition(
                artistId,
                isPastOrDefault(request),
                request.getLastFestivalId(),
                request.getLastStartDate(),
                pageable,
                LocalDate.now(clock)
            )
        );
    }

    private Boolean isPastOrDefault(ArtistFestivalDetailV1Request request) {
        Boolean isPast = request.getPast();
        if (isPast == null) {
            return false;
        }
        return isPast;
    }
}
