package com.festago.socialmedia.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.ImageUrlHelper;
import com.festago.common.util.Validator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "social_media",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"owner_id", "owner_type", "media_type"}
        )
    }
)
public class SocialMedia extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id")
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", columnDefinition = "varchar")
    private OwnerType ownerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", columnDefinition = "varchar")
    private SocialMediaType mediaType;

    private String name;

    private String logoUrl;

    private String url;

    public SocialMedia(Long id, Long ownerId, OwnerType ownerType, SocialMediaType mediaType, String name,
                       String logoUrl, String url) {
        Validator.notNull(ownerId, "ownerId");
        Validator.notNull(ownerType, "ownerType");
        Validator.notNull(mediaType, "mediaType");
        Validator.notBlank(name, "name");
        Validator.notBlank(url, "url");
        this.id = id;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.mediaType = mediaType;
        this.name = name;
        this.logoUrl = ImageUrlHelper.getBlankStringIfBlank(logoUrl);
        this.url = url;
    }

    public SocialMedia(Long ownerId, OwnerType ownerType, SocialMediaType mediaType, String name, String logoUrl,
                       String url) {
        this(null, ownerId, ownerType, mediaType, name, logoUrl, url);
    }

    public void changeName(String name) {
        Validator.notBlank(name, "name");
        this.name = name;
    }

    public void changeUrl(String url) {
        Validator.notBlank(url, "url");
        this.url = url;
    }

    public void changeLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public SocialMediaType getMediaType() {
        return mediaType;
    }

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getUrl() {
        return url;
    }
}
