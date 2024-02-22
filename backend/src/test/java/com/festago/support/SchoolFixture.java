package com.festago.support;

import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;

public class SchoolFixture {

    private Long id;

    private String domain = "festago.com";

    private String name = "페스타고 대학교";

    private SchoolRegion region = SchoolRegion.서울;

    private String logoUrl = "https://image.com/logo.png";

    private String backgroundImageUrl = "https://image.com/backgroundImage.png";

    private SchoolFixture() {
    }

    public static SchoolFixture school() {
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

    public SchoolFixture logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public SchoolFixture backgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
        return this;
    }

    public School build() {
        return new School(id, domain, name, logoUrl, backgroundImageUrl, region);
    }
}
