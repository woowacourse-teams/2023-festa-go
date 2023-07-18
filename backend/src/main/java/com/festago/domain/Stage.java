package com.festago.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startTime;

    protected Stage() {
    }

    public Stage(String name, LocalDateTime startTime) {
        this.name = name;
        this.startTime = startTime;
    }

    public Stage(Long id, String name, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
