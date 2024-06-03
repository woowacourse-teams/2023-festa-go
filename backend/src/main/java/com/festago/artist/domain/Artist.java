package com.festago.artist.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.ImageUrlHelper;
import com.festago.common.util.Validator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "profile_image_url")
    private String profileImage;

    private String backgroundImageUrl;

    public Artist(Long id, String name, String profileImage, String backgroundImageUrl) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.profileImage = ImageUrlHelper.getBlankStringIfBlank(profileImage);
        this.backgroundImageUrl = ImageUrlHelper.getBlankStringIfBlank(backgroundImageUrl);
    }

    private void validateName(String name) {
        Validator.notBlank(name, "name");
    }

    public Artist(String name, String profileImage, String backgroundImageUrl) {
        this(null, name, profileImage, backgroundImageUrl);
    }

    public void update(String name, String profileImage, String backgroundImageUrl) {
        validateName(name);
        this.name = name;
        this.profileImage = ImageUrlHelper.getBlankStringIfBlank(profileImage);
        this.backgroundImageUrl = ImageUrlHelper.getBlankStringIfBlank(backgroundImageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        Class<?> oEffectiveClass = object instanceof HibernateProxy
            ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass()
            : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        Artist artist = (Artist) object;
        return getId() != null && Objects.equals(getId(), artist.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
            .getPersistentClass()
            .hashCode() : getClass().hashCode();
    }
}
