package com.festago.support;

import com.festago.zfestival.domain.Festival;
import java.time.LocalDate;

public class FestivalFixture {

    private Long id;
    private String name = "테코 대학교";
    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now().plusDays(3L);
    private String thumbnail = "https://picsum.photos/536/354";

    private FestivalFixture() {
    }

    public static FestivalFixture festival() {
        return new FestivalFixture();
    }

    public FestivalFixture id(Long id) {
        this.id = id;
        return this;
    }

    public FestivalFixture name(String name) {
        this.name = name;
        return this;
    }

    public FestivalFixture startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public FestivalFixture endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public FestivalFixture thumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public Festival build() {
        return new Festival(id, name, startDate, endDate, thumbnail);
    }
}
