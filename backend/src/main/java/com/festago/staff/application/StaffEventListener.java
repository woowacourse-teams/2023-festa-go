package com.festago.staff.application;

import com.festago.festival.dto.event.FestivalCreateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StaffEventListener {

    private final StaffService staffService;

    public StaffEventListener(StaffService staffService) {
        this.staffService = staffService;
    }

    @EventListener
    public void createStaff(FestivalCreateEvent event) {
        staffService.createStaff(event.festivalId());
    }
}
