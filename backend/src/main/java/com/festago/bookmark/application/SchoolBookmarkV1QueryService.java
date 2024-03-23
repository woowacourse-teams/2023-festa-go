package com.festago.bookmark.application;

import com.festago.bookmark.dto.SchoolBookmarkTotalV1Response;
import com.festago.bookmark.repository.SchoolBookmarkV1QuerydslRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolBookmarkV1QueryService {

    private final SchoolBookmarkV1QuerydslRepository schoolBookmarkV1QuerydslRepository;

    public List<SchoolBookmarkTotalV1Response> findAllByMemberId(Long memberId) {
        return schoolBookmarkV1QuerydslRepository.findAllByMemberId(memberId);
    }
}

