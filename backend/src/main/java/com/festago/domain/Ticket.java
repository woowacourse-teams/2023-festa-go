package com.festago.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Ticket {

    private static final int ENTRY_LIMIT_DAY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime entryTime;

    protected Ticket() {
    }

    public Ticket(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public Ticket(Long id, LocalDateTime entryTime) {
        this.id = id;
        this.entryTime = entryTime;
    }

    public boolean canEntry(LocalDateTime time) {
        return time.isAfter(entryTime) && time.isBefore(entryTime.plusDays(ENTRY_LIMIT_DAY));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
}
