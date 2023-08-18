package com.festago.support;

import com.festago.domain.Festival;
import com.festago.domain.Stage;
import java.time.LocalDateTime;

public class StageFixture {

    private Long id;
    private LocalDateTime startTime = LocalDateTime.now();
    private String lineUp = "오리, 글렌, 푸우, 애쉬";
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

    public StageFixture lineUp(String lineUp) {
        this.lineUp = lineUp;
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
        return new Stage(id, startTime, lineUp, ticketOpenTime, festival);
    }
}
