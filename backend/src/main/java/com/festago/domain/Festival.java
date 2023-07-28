package com.festago.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Festival {

    private static final String DEFAULT_THUMBNAIL = "https://picsum.photos/536/354";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String thumbnail;

    protected Festival() {
    }

    public Festival(String name, LocalDate startDate, LocalDate endDate) {
        this(null, name, startDate, endDate, DEFAULT_THUMBNAIL);
    }

    public Festival(String name, LocalDate startDate, LocalDate endDate, String thumbnail) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnail = thumbnail;
    }

    public Festival(Long id, String name, LocalDate startDate, LocalDate endDate, String thumbnail) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnail = thumbnail;
    }

    public boolean isNotInDuration(LocalDateTime time) {
        LocalDate date = time.toLocalDate();
        return date.isBefore(startDate) || date.isAfter(endDate);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
