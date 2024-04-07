package com.festago.admin.application;

import com.festago.admin.dto.artist.AdminArtistV1Response;
import com.festago.admin.repository.AdminArtistV1QueryDslRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.querydsl.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminArtistV1QueryService {

    private final AdminArtistV1QueryDslRepository adminArtistV1QueryDslRepository;

    public AdminArtistV1Response findById(Long artistId) {
        return adminArtistV1QueryDslRepository.findById(artistId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ARTIST_NOT_FOUND));
    }

    public Page<AdminArtistV1Response> findAll(SearchCondition searchCondition) {
        return adminArtistV1QueryDslRepository.findAll(searchCondition);
    }
}
