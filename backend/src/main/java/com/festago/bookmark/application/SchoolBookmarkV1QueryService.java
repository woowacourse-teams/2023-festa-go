package com.festago.bookmark.application;

import com.festago.bookmark.dto.v1.SchoolBookmarkV1Response;
import com.festago.bookmark.repository.v1.SchoolBookmarkV1QuerydslRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchoolBookmarkV1QueryService {

    private final SchoolBookmarkV1QuerydslRepository schoolBookmarkV1QuerydslRepository;

    public List<SchoolBookmarkV1Response> findAllByMemberId(Long memberId) {
        return schoolBookmarkV1QuerydslRepository.findAllByMemberId(memberId);
    }
}

