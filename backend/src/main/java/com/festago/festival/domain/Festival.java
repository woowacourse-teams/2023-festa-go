package com.festago.festival.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.ValidException;
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
        Validator.notNull(name, "name은 null 값이 될 수 없습니다.");
        Validator.maxLength(name, 50, "name은 50글자를 넘을 수 없습니다.");
    }

    private void validateThumbnail(String thumbnail) {
        Validator.notNull(thumbnail, "thumbnail은 null 값이 될 수 없습니다.");
        Validator.maxLength(thumbnail, 255, "thumbnail은 50글자를 넘을 수 없습니다.");
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
        Validator.notNull(startDate, "startDate는 null 값이 될 수 없습니다.");
        Validator.notNull(endDate, "endDate는 null 값이 될 수 없습니다.");
        if (startDate.isAfter(endDate)) {
            throw new ValidException("축제 시작 일은 종료일 이전이어야 합니다.");
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
