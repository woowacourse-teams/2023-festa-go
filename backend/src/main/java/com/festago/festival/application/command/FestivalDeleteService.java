package com.festago.festival.application.command;

import com.festago.festival.domain.validator.FestivalDeleteValidator;
import com.festago.festival.dto.event.FestivalDeletedEvent;
import com.festago.festival.repository.FestivalRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalDeleteService {

    private final FestivalRepository festivalRepository;
    private final List<FestivalDeleteValidator> validators;
    private final ApplicationEventPublisher eventPublisher;

    public void deleteFestival(Long festivalId) {
        validators.forEach(validator -> validator.validate(festivalId));
        festivalRepository.deleteById(festivalId);
        eventPublisher.publishEvent(new FestivalDeletedEvent(festivalId));
    }
}
