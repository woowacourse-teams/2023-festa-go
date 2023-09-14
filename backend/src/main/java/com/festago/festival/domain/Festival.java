package com.festago.festival.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Festival extends BaseTimeEntity {

    private static final String DEFAULT_THUMBNAIL = "https://picsum.photos/536/354";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @Size(max = 255)
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
        validate(name, startDate, endDate, thumbnail);
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnail = thumbnail;
    }

    private void validate(String name, LocalDate startDate, LocalDate endDate, String thumbnail) {
        checkNotNull(name, startDate, endDate, thumbnail);
        checkLength(name, thumbnail);
        validateDate(startDate, endDate);
    }

    private void checkNotNull(String name, LocalDate startDate, LocalDate endDate, String thumbnail) {
        if (name == null ||
            startDate == null ||
            endDate == null ||
            thumbnail == null) {
            throw new IllegalArgumentException("Festival 은 허용되지 않은 null 값으로 생성할 수 없습니다.");
        }
    }

    private void checkLength(String name, String thumbnail) {
        if (overLength(name, 50) ||
            overLength(thumbnail, 255)) {
            throw new IllegalArgumentException("Festival 의 필드로 허용된 길이를 넘은 column 을 넣을 수 없습니다.");
        }
    }

    private boolean overLength(String target, int maxLength) {
        if (target == null) {
            return false;
        }
        return target.length() > maxLength;
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
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
