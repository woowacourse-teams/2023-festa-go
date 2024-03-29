package com.festago.admin.application;

import com.festago.admin.dto.stage.AdminStageV1Response;
import com.festago.admin.repository.AdminStageV1QueryDslRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminStageV1QueryService {

    private final AdminStageV1QueryDslRepository adminStageV1QueryDslRepository;

    public List<AdminStageV1Response> findAllByFestivalId(Long festivalId) {
        return adminStageV1QueryDslRepository.findAllByFestivalId(festivalId);
    }
}
