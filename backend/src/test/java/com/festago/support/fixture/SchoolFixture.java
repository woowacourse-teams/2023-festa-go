package com.festago.support.fixture;

import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;

public class SchoolFixture extends BaseFixture {

    private Long id;
    private String domain;
    private String name;
    private SchoolRegion region = SchoolRegion.서울;
    private String logoUrl = "https://image.com/logo.png";
    private String backgroundImageUrl = "https://image.com/backgroundImage.png";

    private SchoolFixture() {
    }

    public static SchoolFixture builder() {
        return new SchoolFixture();
    }

    public SchoolFixture id(Long id) {
        this.id = id;
        return this;
    }

    public SchoolFixture domain(String domain) {
        this.domain = domain;
        return this;
    }

    public SchoolFixture name(String name) {
        this.name = name;
        return this;
    }

    public SchoolFixture region(SchoolRegion region) {
        this.region = region;
        return this;
    }

    public SchoolFixture logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public SchoolFixture backgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
        return this;
    }

    public School build() {
        return new School(
            id,
            uniqueValue("festago.com", domain),
            uniqueValue("페스타고 대학교", name),
            logoUrl,
            backgroundImageUrl,
            region
        );
    }
}
