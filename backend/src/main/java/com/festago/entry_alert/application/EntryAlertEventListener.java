package com.festago.entry_alert.application;

import com.festago.entry_alert.dto.EntryAlertResponse;
import com.festago.ticket.dto.event.TicketCreateEvent;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EntryAlertEventListener {

    private final EntryAlertService entryAlertService;
    private final TaskScheduler taskScheduler;
    private final Clock clock;

    public EntryAlertEventListener(EntryAlertService entryAlertService, TaskScheduler taskScheduler, Clock clock) {
        this.entryAlertService = entryAlertService;
        this.taskScheduler = taskScheduler;
        this.clock = clock;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void addEntryAlertSchedule(TicketCreateEvent event) {
        EntryAlertResponse entryAlert = entryAlertService.create(event.stageId(), event.entryTime());
        Runnable task = createEntryAlertTask(entryAlert.id());
        LocalDateTime alertTime = entryAlert.alertTime();
        taskScheduler.schedule(task, alertTime.atZone(clock.getZone()).toInstant());
    }

    private Runnable createEntryAlertTask(Long id) {
        return () -> entryAlertService.sendEntryAlert(id);
    }
}
