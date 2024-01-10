package com.festago.festival.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.util.Validator;
import com.festago.school.domain.School;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Festival extends BaseTimeEntity {

    private static final String DEFAULT_THUMBNAIL = "https://picsum.photos/536/354";
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_THUMBNAIL_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = MAX_NAME_LENGTH)
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @Size(max = MAX_THUMBNAIL_LENGTH)
    private String thumbnail;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    public Festival(String name, LocalDate startDate, LocalDate endDate, School school) {
        this(null, name, startDate, endDate, DEFAULT_THUMBNAIL, school);
    }

    public Festival(String name, LocalDate startDate, LocalDate endDate, String thumbnail, School school) {
        this(null, name, startDate, endDate, thumbnail, school);
    }

    public Festival(Long id, String name, LocalDate startDate, LocalDate endDate, String thumbnail, School school) {
        validate(name, startDate, endDate, thumbnail);
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnail = thumbnail;
        this.school = school;
    }

    private void validate(String name, LocalDate startDate, LocalDate endDate, String thumbnail) {
        validateName(name);
        validateThumbnail(thumbnail);
        validateDate(startDate, endDate);
    }

    private void validateName(String name) {
        String fieldName = "name";
        Validator.hasBlank(name, fieldName);
        Validator.maxLength(name, MAX_NAME_LENGTH, fieldName);
    }

    private void validateThumbnail(String thumbnail) {
        String fieldName = "thumbnail";
        Validator.hasBlank(thumbnail, fieldName);
        Validator.maxLength(thumbnail, MAX_THUMBNAIL_LENGTH, fieldName);
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
        Validator.notNull(startDate, "startDate");
        Validator.notNull(endDate, "endDate");
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

    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    public void changeThumbnail(String thumbnail) {
        validateThumbnail(thumbnail);
        this.thumbnail = thumbnail;
    }

    public void changeDate(LocalDate startDate, LocalDate endDate) {
        validateDate(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
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

    public School getSchool() {
        return school;
    }
}
