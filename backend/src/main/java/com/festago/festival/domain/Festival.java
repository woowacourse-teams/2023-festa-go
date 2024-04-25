package com.festago.festival.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.Validator;
import com.festago.school.domain.School;
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

    private static final String DEFAULT_POSTER_IMAGE_URL = "https://picsum.photos/536/354";
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_POSTER_IMAGE_URL_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = MAX_NAME_LENGTH)
    private String name;

    @Embedded
    private FestivalDuration festivalDuration;

    @NotNull
    @Size(max = MAX_POSTER_IMAGE_URL_LENGTH)
    private String posterImageUrl;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    public Festival(String name, FestivalDuration festivalDuration, School school) {
        this(null, name, festivalDuration, DEFAULT_POSTER_IMAGE_URL, school);
    }

    public Festival(String name, FestivalDuration festivalDuration, String posterImageUrl, School school) {
        this(null, name, festivalDuration, posterImageUrl, school);
    }

    public Festival(Long id, String name, FestivalDuration festivalDuration, String posterImageUrl, School school) {
        validateName(name);
        validateFestivalDuration(festivalDuration);
        validatePosterImageUrl(posterImageUrl);
        validateSchool(school);
        this.id = id;
        this.name = name;
        this.festivalDuration = festivalDuration;
        this.posterImageUrl = posterImageUrl;
        this.school = school;
    }

    private void validateName(String name) {
        String fieldName = "name";
        Validator.notBlank(name, fieldName);
        Validator.maxLength(name, MAX_NAME_LENGTH, fieldName);
    }

    private void validatePosterImageUrl(String posterImageUrl) {
        String fieldName = "posterImageUrl";
        Validator.notBlank(posterImageUrl, fieldName);
        Validator.maxLength(posterImageUrl, MAX_POSTER_IMAGE_URL_LENGTH, fieldName);
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

    public void changePosterImageUrl(String posterImageUrl) {
        validatePosterImageUrl(posterImageUrl);
        this.posterImageUrl = posterImageUrl;
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

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public School getSchool() {
        return school;
    }
}
