package com.festago.entryalert.application;

import com.festago.entryalert.dto.EntryAlertResponse;
import com.festago.ticket.dto.event.TicketCreateEvent;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EntryAlertEventListener {

    private static final Logger log = LoggerFactory.getLogger(EntryAlertEventListener.class);

    private final EntryAlertService entryAlertService;
    private final TaskScheduler taskScheduler;
    private final Clock clock;

    public EntryAlertEventListener(EntryAlertService entryAlertService, TaskScheduler taskScheduler, Clock clock) {
        this.entryAlertService = entryAlertService;
        this.taskScheduler = taskScheduler;
        this.clock = clock;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initEntryAlertSchedule() {
        List<EntryAlertResponse> entryAlerts = entryAlertService.findAllPending();
        entryAlerts.forEach(this::addSchedule);
    }

    private void addSchedule(EntryAlertResponse entryAlert) {
        Long entryAlertId = entryAlert.id();
        log.info("add entryAlert schedule: {}", entryAlertId);
        Instant alertTime = toInstant(entryAlert.alertTime());
        taskScheduler.schedule(() -> entryAlertService.sendEntryAlert(entryAlertId), alertTime);
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(clock.getZone()).toInstant();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void addEntryAlertSchedule(TicketCreateEvent event) {
        EntryAlertResponse entryAlert = entryAlertService.create(event.stageId(), event.entryTime());
        addSchedule(entryAlert);
    }
}
