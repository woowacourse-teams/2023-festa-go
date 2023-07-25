package com.festago.support;

import com.festago.domain.Stage;
import java.time.LocalDateTime;

public class StageFixture {

    private Long id = 1L;
    private String name = "테코 대학교 축제";
    private LocalDateTime startTime = LocalDateTime.now();

    private StageFixture() {
    }

    public static StageFixture stage() {
        return new StageFixture();
    }

    public StageFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StageFixture name(String name) {
        this.name = name;
        return this;
    }

    public StageFixture startTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public Stage build() {
        return new Stage(id, name, startTime);
    }
}
