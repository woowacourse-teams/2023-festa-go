package com.festago.festival.application;

import com.festago.festival.dto.FestivalSearchV1Response;
import com.festago.festival.repository.ArtistFestivalSearchV1QueryDslRepository;
import com.festago.festival.repository.SchoolFestivalSearchV1QueryDslRepository;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FestivalSearchV1QueryService {

    private static final Pattern SCHOOL_PATTERN = Pattern.compile(" .*대(학교)?$");

    private final ArtistFestivalSearchV1QueryDslRepository artistFestivalSearchV1QueryDslRepository;
    private final SchoolFestivalSearchV1QueryDslRepository schoolFestivalSearchV1QueryDslRepository;

    public List<FestivalSearchV1Response> search(String keyword) {
        Matcher schoolMatcher = SCHOOL_PATTERN.matcher(keyword);
        if (schoolMatcher.matches()) {
            return schoolFestivalSearchV1QueryDslRepository.executeSearch(keyword);
        }
        return artistFestivalSearchV1QueryDslRepository.executeSearch(keyword);
    }
}
