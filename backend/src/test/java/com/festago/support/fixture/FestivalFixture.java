package com.festago.support.fixture;

import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import java.time.LocalDate;

public class FestivalFixture extends BaseFixture {

    private Long id;
    private String name;
    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now().plusDays(3L);
    private String posterImageUrl = "https://picsum.photos/536/354";
    private School school = SchoolFixture.builder().build();

    private FestivalFixture() {
    }

    public static FestivalFixture builder() {
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

    public FestivalFixture posterImageUrl(String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
        return this;
    }

    public FestivalFixture school(School school) {
        this.school = school;
        return this;
    }

    public Festival build() {
        return new Festival(id, uniqueValue("페스타고 대학교 축제", name), startDate, endDate, posterImageUrl, school);
    }
}
