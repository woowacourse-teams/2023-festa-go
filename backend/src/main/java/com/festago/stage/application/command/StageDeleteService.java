package com.festago.stage.application.command;

import com.festago.stage.dto.event.StageDeletedEvent;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StageDeleteService {

    private final StageRepository stageRepository;
    private final StageArtistRepository stageArtistRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void deleteStage(Long stageId) {
        stageRepository.findByIdWithFetch(stageId).ifPresent(stage -> {
            stageRepository.deleteById(stageId);
            stageArtistRepository.deleteByStageId(stageId);
            // TODO
            // delete 호출하면 엔티티가 비영속 상태로 변하게 됨
            // 그럼에도 이벤트로 엔티티를 넘겨주는 것이 맞는지..?
            // 사용하는 곳에서 삭제 이벤트를 받았으니, 수정을 하지 않으면 상관 없지 않을까?
            // 수정을 해도, 비영속 상태니까 사이드 이펙트는 발생하지 않을 것 같음.
            eventPublisher.publishEvent(new StageDeletedEvent(stage));
        });
    }
}
