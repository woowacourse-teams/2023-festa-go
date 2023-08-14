package com.festago.domain;

import com.festago.auth.domain.SocialType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE member SET deleted_at = now(), nickname = '탈퇴한 회원', profile_image = '' WHERE id=?")
@Where(clause = "deleted_at is null")
public class Member extends BaseTimeEntity {

    private static final String DEFAULT_IMAGE_URL = "https://festa-go.site/images/default-profile.png";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    private String socialId;

    @NotNull
    private SocialType socialType;

    @NotNull
    @Size(max = 30)
    private String nickname;

    @NotNull
    @Size(max = 255)
    private String profileImage;

    private LocalDateTime deletedAt = null;

    protected Member() {
    }

    public Member(Long id) {
        this.id = id;
    }

    public Member(String socialId, SocialType socialType, String nickname, String profileImage) {
        this(null, socialId, socialType, nickname, profileImage);
    }

    public Member(Long id, String socialId, SocialType socialType, String nickname, String profileImage) {
        this.id = id;
        this.socialId = socialId;
        this.socialType = socialType;
        this.nickname = nickname;
        this.profileImage = (profileImage != null) ? profileImage : DEFAULT_IMAGE_URL;
    }

    public Long getId() {
        return id;
    }

    public String getSocialId() {
        return socialId;
    }

    public SocialType getSocialType() {
        return socialType;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
