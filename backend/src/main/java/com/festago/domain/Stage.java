package com.festago.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Festival festival;

    protected Stage() {
    }

    public Stage(LocalDateTime startTime, Festival festival) {
        this(null, startTime, festival);
    }

    public Stage(Long id, LocalDateTime startTime, Festival festival) {
        this.id = id;
        this.startTime = startTime;
        this.festival = festival;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Festival getFestival() {
        return festival;
    }
}
