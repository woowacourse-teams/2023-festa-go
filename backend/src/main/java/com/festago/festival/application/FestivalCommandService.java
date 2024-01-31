package com.festago.festival.application;

import com.festago.festival.dto.command.FestivalCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalCommandService {

    public Long createFestival(FestivalCreateCommand command) {
        return null;
    }
}
