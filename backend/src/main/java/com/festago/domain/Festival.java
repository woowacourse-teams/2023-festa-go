package com.festago.domain;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Festival extends BaseTimeEntity {

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
        this(null, name, startDate, endDate, thumbnail);
    }

    public Festival(Long id, String name, LocalDate startDate, LocalDate endDate, String thumbnail) {
        validate(startDate, endDate);
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnail = thumbnail;
    }

    private void validate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException(ErrorCode.INVALID_FESTIVAL_DURATION);
        }
    }

    public boolean canCreate(LocalDate currentDate) {
        return startDate.isEqual(currentDate) || startDate.isAfter(currentDate);
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
