package com.festago.bookmark.application;

import com.festago.bookmark.dto.v1.ArtistBookmarkV1Response;
import com.festago.bookmark.repository.v1.ArtistBookmarkV1QueryDslRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistBookmarkV1QueryService {

    private final ArtistBookmarkV1QueryDslRepository artistBookmarkV1QueryDslRepository;

    public List<ArtistBookmarkV1Response> findArtistBookmarksByMemberId(Long memberid){
        return artistBookmarkV1QueryDslRepository.findByMemberId(memberid);
    }
}
