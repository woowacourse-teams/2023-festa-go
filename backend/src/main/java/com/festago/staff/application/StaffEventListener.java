package com.festago.staff.application;

import com.festago.festival.dto.event.FestivalCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StaffEventListener {

    private final StaffService staffService;
    
    @EventListener
    public void createStaff(FestivalCreateEvent event) {
        staffService.createStaff(event.festivalId());
    }
}
