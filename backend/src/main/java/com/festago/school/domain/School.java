package com.festago.school.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.Validator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class School extends BaseTimeEntity {

    private static final String DEFAULT_URL = "https://picsum.photos/536/354";
    private static final int MAX_DOMAIN_LENGTH = 50;
    private static final int MAX_NAME_LENGTH = 255;
    private static final int MAX_IMAGE_URL_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = MAX_DOMAIN_LENGTH)
    @Column(unique = true)
    private String domain;

    @NotNull
    @Size(max = MAX_NAME_LENGTH)
    @Column(unique = true)
    private String name;

    private String logoUrl;

    private String backgroundUrl;

    @Enumerated(EnumType.STRING)
    private SchoolRegion region;

    public School(Long id, String domain, String name, String logoUrl, String backgroundImageUrl, SchoolRegion region) {
        validate(domain, name, region, logoUrl, backgroundImageUrl);
        this.id = id;
        this.domain = domain;
        this.name = name;
        this.logoUrl = getDefaultUrlIfBlank(logoUrl);
        this.backgroundUrl = getDefaultUrlIfBlank(backgroundImageUrl);
        this.region = region;
    }

    private String getDefaultUrlIfBlank(String imageUrl) {
        if (StringUtils.hasText(imageUrl)) {
            return imageUrl;
        }
        return DEFAULT_URL;
    }

    public School(String domain, String name, SchoolRegion region) {
        this(null, domain, name, DEFAULT_URL, DEFAULT_URL, region);
    }

    private void validate(String domain, String name, SchoolRegion region, String logoUrl, String backgroundImageUrl) {
        validateDomain(domain);
        validateName(name);
        validateRegion(region);
        validateImageUrl(logoUrl, "logoUrl");
        validateImageUrl(backgroundImageUrl, "backgroundImageUrl");
    }

    private void validateDomain(String domain) {
        String fieldName = "domain";
        Validator.hasBlank(domain, fieldName);
        Validator.maxLength(domain, MAX_DOMAIN_LENGTH, fieldName);
    }

    private void validateName(String name) {
        String fieldName = "name";
        Validator.hasBlank(name, fieldName);
        Validator.maxLength(name, MAX_NAME_LENGTH, fieldName);
    }

    private void validateRegion(SchoolRegion region) {
        Validator.notNull(region, "region");
    }

    private void validateImageUrl(String logoUrl, String fieldName) {
        Validator.maxLength(logoUrl, MAX_IMAGE_URL_LENGTH, fieldName);
    }

    public void changeDomain(String domain) {
        validateDomain(domain);
        this.domain = domain;
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    public void changeRegion(SchoolRegion region) {
        validateRegion(region);
        this.region = region;
    }

    public void changeLogoUrl(String logoUrl) {
        validateImageUrl(logoUrl, "logoUrl");
        this.logoUrl = getDefaultUrlIfBlank(logoUrl);
    }

    public void changeBackgroundImageUrl(String backgroundImageUrl) {
        validateImageUrl(backgroundImageUrl, "backgroundImageUrl");
        this.backgroundUrl = getDefaultUrlIfBlank(backgroundImageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public SchoolRegion getRegion() {
        return region;
    }
}
