package com.festago.entryalert.application;

import com.festago.entryalert.dto.EntryAlertResponse;
import com.festago.ticket.dto.event.TicketCreateEvent;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntryAlertEventListener {

    private final EntryAlertService entryAlertService;
    private final TaskScheduler taskScheduler;
    private final Clock clock;

    @EventListener(ApplicationReadyEvent.class)
    public void initEntryAlertSchedule() {
        List<EntryAlertResponse> entryAlerts = entryAlertService.findAllPending();
        entryAlerts.forEach(this::addSchedule);
    }

    private void addSchedule(EntryAlertResponse entryAlert) {
        Long entryAlertId = entryAlert.id();
        Instant alertTime = toInstant(entryAlert.alertTime());
        log.info("EntryAlert 스케쥴링 추가. entryAlertId: {}, alertTime: {}", entryAlertId, entryAlert.alertTime());
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
