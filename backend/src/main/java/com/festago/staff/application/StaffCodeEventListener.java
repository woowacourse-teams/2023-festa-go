package com.festago.staff.application;

import com.festago.festival.dto.event.FestivalCreateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StaffCodeEventListener {

    private final StaffService staffService;

    public StaffCodeEventListener(StaffService staffService) {
        this.staffService = staffService;
    }

    @EventListener
    public void createStaffCode(FestivalCreateEvent event) {
        staffService.createStaffCode(event.festivalId());
    }
}
