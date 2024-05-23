package com.festago.admin.application;

import com.festago.admin.dto.stage.AdminStageV1Response;
import com.festago.admin.repository.AdminStageV1QueryDslRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
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

    public AdminStageV1Response findById(Long stageId) {
        return adminStageV1QueryDslRepository.findById(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
    }
}
