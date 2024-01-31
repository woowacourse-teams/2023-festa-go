package com.festago.festival.application;

import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.dto.event.FestivalCreatedEvent;
import com.festago.festival.repository.FestivalQueryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FestivalQueryInfoEventListener {

    private final FestivalQueryInfoRepository festivalQueryInfoRepository;

    /**
     * 해당 이벤트는 비동기로 실행하면 문제가 발생할 수 있으니, 동기적으로 처리해야함 <br/> 축제가 생성되면 FestivalQueryInfo는 반드시! 생성되어야 함
     */
    @EventListener
    @Transactional(propagation = Propagation.SUPPORTS)
    public void festivalCreateEventHandler(FestivalCreatedEvent event) {
        FestivalQueryInfo festivalQueryInfo = FestivalQueryInfo.create(event.festivalId());
        festivalQueryInfoRepository.save(festivalQueryInfo);
    }
}
