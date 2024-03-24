package com.festago.support.fixture;

import com.festago.festival.domain.Festival;
import com.festago.stage.domain.Stage;
import java.time.LocalDateTime;

public class StageFixture extends BaseFixture {

    private Long id;
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime ticketOpenTime;
    private Festival festival = FestivalFixture.builder().build();

    private StageFixture() {
    }

    public static StageFixture builder() {
        return new StageFixture();
    }

    public StageFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StageFixture startTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public StageFixture ticketOpenTime(LocalDateTime ticketOpenTime) {
        this.ticketOpenTime = ticketOpenTime;
        return this;
    }

    public StageFixture festival(Festival festival) {
        this.festival = festival;
        return this;
    }

    public Stage build() {
        return new Stage(
            id,
            startTime,
            ticketOpenTime == null ? startTime.minusWeeks(1) : ticketOpenTime,
            festival
        );
    }
}
