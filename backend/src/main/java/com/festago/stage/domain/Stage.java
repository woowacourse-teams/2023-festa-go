package com.festago.stage.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.util.Validator;
import com.festago.festival.domain.Festival;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime ticketOpenTime;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Festival festival;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stageId", orphanRemoval = true,
        cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<StageArtist> artists = new ArrayList<>();

    public Stage(LocalDateTime startTime, LocalDateTime ticketOpenTime, Festival festival) {
        this(null, startTime, ticketOpenTime, festival);
    }

    public Stage(Long id, LocalDateTime startTime, LocalDateTime ticketOpenTime,
                 Festival festival) {
        validate(startTime, ticketOpenTime, festival);
        this.id = id;
        this.startTime = startTime;
        this.ticketOpenTime = ticketOpenTime;
        this.festival = festival;
    }

    private void validate(LocalDateTime startTime, LocalDateTime ticketOpenTime, Festival festival) {
        validateFestival(festival);
        validateTime(startTime, ticketOpenTime, festival);
    }

    private void validateFestival(Festival festival) {
        Validator.notNull(festival, "festival");
    }

    private void validateTime(LocalDateTime startTime, LocalDateTime ticketOpenTime, Festival festival) {
        Validator.notNull(startTime, "startTime");
        Validator.notNull(ticketOpenTime, "ticketOpenTime");
        if (ticketOpenTime.isAfter(startTime) || ticketOpenTime.isEqual(startTime)) {
            throw new BadRequestException(ErrorCode.INVALID_TICKET_OPEN_TIME);
        }
        if (festival.isNotInDuration(startTime)) {
            throw new BadRequestException(ErrorCode.INVALID_STAGE_START_TIME);
        }
    }

    public boolean isStart(LocalDateTime currentTime) {
        return currentTime.isAfter(startTime);
    }

    public boolean isBeforeTicketOpenTime(LocalDateTime currentTime) {
        return currentTime.isBefore(ticketOpenTime);
    }

    public void changeTime(LocalDateTime startTime, LocalDateTime ticketOpenTime) {
        validateTime(startTime, ticketOpenTime, this.festival);
        this.startTime = startTime;
        this.ticketOpenTime = ticketOpenTime;
    }

    public void renewArtists(List<Long> artistIds) {
        artists.removeIf(artist -> !artistIds.contains(artist.getArtistId()));
        Set<Long> existsArtistIds = artists.stream()
            .map(StageArtist::getArtistId)
            .collect(Collectors.toSet());
        for (Long artistId : artistIds) {
            if (!existsArtistIds.contains(artistId)) {
                artists.add(new StageArtist(this.id, artistId));
            }
        }
    }

    public List<Long> getArtistIds() {
        return artists.stream()
            .map(StageArtist::getArtistId)
            .sorted()
            .toList();
    }

    // 디미터 법칙에 어긋나지만, n+1을 회피하고, fetch join을 생략하며 주인을 검사하기 위해 getter 체이닝 사용
    public boolean isSchoolStage(Long schoolId) {
        return Objects.equals(getFestival().getSchool().getId(), schoolId);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getTicketOpenTime() {
        return ticketOpenTime;
    }

    public Festival getFestival() {
        return festival;
    }
}
