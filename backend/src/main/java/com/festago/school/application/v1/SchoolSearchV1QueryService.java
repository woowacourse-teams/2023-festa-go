package com.festago.school.application.v1;

import com.festago.school.dto.v1.SchoolSearchV1Response;
import com.festago.school.repository.v1.SchoolSearchV1QueryDslRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchoolSearchV1QueryService {

    private final SchoolSearchV1QueryDslRepository schoolSearchV1QueryDslRepository;

    public List<SchoolSearchV1Response> searchSchools(String keyword) {
        return schoolSearchV1QueryDslRepository.searchSchools(keyword);
    }
}
