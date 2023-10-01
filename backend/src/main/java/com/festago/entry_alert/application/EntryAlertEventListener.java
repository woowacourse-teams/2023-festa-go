package com.festago.entry_alert.application;

import com.festago.entry_alert.domain.EntryAlert;
import com.festago.entry_alert.repository.EntryAlertRepository;
import com.festago.ticket.dto.event.TicketCreateEvent;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Profile({"dev", "prod"})
public class EntryAlertEventListener {

    private final EntryAlertRepository entryAlertRepository;
    private final EntryAlertService entryAlertService;
    private final TaskScheduler taskScheduler;
    private final Clock clock;

    public EntryAlertEventListener(EntryAlertRepository entryAlertRepository, EntryAlertService entryAlertService,
                                   TaskScheduler taskScheduler, Clock clock) {
        this.entryAlertRepository = entryAlertRepository;
        this.entryAlertService = entryAlertService;
        this.taskScheduler = taskScheduler;
        this.clock = clock;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void addEntryAlertSchedule(TicketCreateEvent event) {
        EntryAlert entryAlert = entryAlertRepository.save(new EntryAlert(event.stageId(), event.entryTime()));
        Runnable task = createEntryAlertTask(entryAlert.getId());
        LocalDateTime alertTime = entryAlert.findAlertTime();
        taskScheduler.schedule(task, alertTime.atZone(clock.getZone()).toInstant());
    }

    private Runnable createEntryAlertTask(Long id) {
        return () -> entryAlertService.sendEntryAlert(id);
    }
}
