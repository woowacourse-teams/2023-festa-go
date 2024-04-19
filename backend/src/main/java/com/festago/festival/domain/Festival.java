package com.festago.festival.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.Validator;
import com.festago.school.domain.School;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

    @Embedded
    private FestivalDuration festivalDuration;

    @NotNull
    @Size(max = MAX_THUMBNAIL_LENGTH)
    @Column(name = "poster_image_url")
    private String thumbnail;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    public Festival(String name, FestivalDuration festivalDuration, School school) {
        this(null, name, festivalDuration, DEFAULT_THUMBNAIL, school);
    }

    public Festival(String name, FestivalDuration festivalDuration, String thumbnail, School school) {
        this(null, name, festivalDuration, thumbnail, school);
    }

    public Festival(Long id, String name, FestivalDuration festivalDuration, String thumbnail, School school) {
        validateName(name);
        validateFestivalDuration(festivalDuration);
        validateThumbnail(thumbnail);
        validateSchool(school);
        this.id = id;
        this.name = name;
        this.festivalDuration = festivalDuration;
        this.thumbnail = thumbnail;
        this.school = school;
    }

    private void validateName(String name) {
        String fieldName = "name";
        Validator.notBlank(name, fieldName);
        Validator.maxLength(name, MAX_NAME_LENGTH, fieldName);
    }

    private void validateThumbnail(String thumbnail) {
        String fieldName = "thumbnail";
        Validator.notBlank(thumbnail, fieldName);
        Validator.maxLength(thumbnail, MAX_THUMBNAIL_LENGTH, fieldName);
    }

    private void validateFestivalDuration(FestivalDuration festivalDuration) {
        Validator.notNull(festivalDuration, "festivalDuration");
    }

    private void validateSchool(School school) {
        Validator.notNull(school, "school");
    }

    public boolean isStartDateBeforeTo(LocalDate date) {
        return festivalDuration.isStartDateBeforeTo(date);
    }

    public boolean isNotInDuration(LocalDateTime dateTime) {
        return festivalDuration.isNotInDuration(dateTime.toLocalDate());
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    public void changeThumbnail(String thumbnail) {
        validateThumbnail(thumbnail);
        this.thumbnail = thumbnail;
    }

    @Deprecated(forRemoval = true)
    public void changeDate(LocalDate startDate, LocalDate endDate) {
        this.festivalDuration = new FestivalDuration(startDate, endDate);
    }

    public void changeFestivalDuration(FestivalDuration festivalDuration) {
        validateFestivalDuration(festivalDuration);
        this.festivalDuration = festivalDuration;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return festivalDuration.getStartDate();
    }

    public LocalDate getEndDate() {
        return festivalDuration.getEndDate();
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public School getSchool() {
        return school;
    }
}
