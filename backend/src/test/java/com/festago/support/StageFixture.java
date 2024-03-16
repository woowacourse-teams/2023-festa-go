package com.festago.support;

import com.festago.festival.domain.Festival;
import com.festago.stage.domain.Stage;
import java.time.LocalDateTime;

public class StageFixture {

    private Long id;
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime ticketOpenTime = startTime.minusWeeks(1);
    private Festival festival = FestivalFixture.festival().build();

    private StageFixture() {
    }

    public static StageFixture stage() {
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
        return new Stage(id, startTime, ticketOpenTime, festival);
    }
}
