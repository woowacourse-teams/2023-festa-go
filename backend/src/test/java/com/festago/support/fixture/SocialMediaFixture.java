package com.festago.support.fixture;

import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;

public class SocialMediaFixture {

    private Long id;
    private Long ownerId;
    private OwnerType ownerType;
    private SocialMediaType mediaType = SocialMediaType.INSTAGRAM;
    private String name = "총학생회 인스타그램";
    private String logoUrl = "https://image.com/logo.png";
    private String url = "https://instagram.com";

    public static SocialMediaFixture builder() {
        return new SocialMediaFixture();
    }

    public SocialMediaFixture id(Long id) {
        this.id = id;
        return this;
    }

    public SocialMediaFixture ownerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public SocialMediaFixture ownerType(OwnerType ownerType) {
        this.ownerType = ownerType;
        return this;
    }

    public SocialMediaFixture mediaType(SocialMediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public SocialMediaFixture name(String name) {
        this.name = name;
        return this;
    }

    public SocialMediaFixture logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public SocialMediaFixture url(String url) {
        this.url = url;
        return this;
    }

    public SocialMedia build() {
        return new SocialMedia(
            id,
            ownerId,
            ownerType,
            mediaType,
            name,
            logoUrl,
            url
        );
    }
}
