package com.festago.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Ticket {

    private static final int ENTRY_LIMIT_DAY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Stage stage;

    private LocalDateTime entryTime;

    protected Ticket() {
    }

    public Ticket(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public Ticket(Stage stage, LocalDateTime entryTime) {
        this.stage = stage;
        this.entryTime = entryTime;
    }

    public Ticket(Long id, Stage stage, LocalDateTime entryTime) {
        this.id = id;
        this.stage = stage;
        this.entryTime = entryTime;
    }

    public boolean canEntry(LocalDateTime time) {
        return time.isAfter(entryTime) && time.isBefore(entryTime.plusDays(ENTRY_LIMIT_DAY));
    }

    public Long getId() {
        return id;
    }

    public Stage getStage() {
        return stage;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
}
