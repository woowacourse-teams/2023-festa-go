package com.festago.artist.application;

import com.festago.artist.dto.ArtistDetailV1Response;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistDetailV1QueryService {

    private final ArtistDetailV1QueryDslRepository artistDetailV1QueryDslRepository;

    public ArtistDetailV1Response findArtistDetail(Long artistId) {
        return artistDetailV1QueryDslRepository.findArtistDetail(artistId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ARTIST_NOT_FOUND));
    }
}
