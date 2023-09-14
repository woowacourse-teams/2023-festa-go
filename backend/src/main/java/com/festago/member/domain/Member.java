package com.festago.member.domain;

import com.festago.auth.domain.SocialType;
import com.festago.common.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(value = EnumType.STRING)
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
        validate(socialId, socialType, nickname, profileImage);
        this.id = id;
        this.socialId = socialId;
        this.socialType = socialType;
        this.nickname = nickname;
        this.profileImage = (profileImage != null) ? profileImage : DEFAULT_IMAGE_URL;
    }

    private void validate(String socialId, SocialType socialType, String nickname, String profileImage) {
        checkNotNull(socialId, socialType, nickname);
        checkLength(socialId, nickname, profileImage);
    }

    private void checkNotNull(String socialId, SocialType socialType, String nickname) {
        if (socialId == null ||
            socialType == null ||
            nickname == null) {
            throw new IllegalArgumentException("Member 는 허용되지 않은 null 값으로 생성할 수 없습니다.");
        }
    }

    private void checkLength(String socialId, String nickname, String profileImage) {
        if (overLength(socialId, 255) ||
            overLength(nickname, 30) ||
            overLength(profileImage, 255)) {
            throw new IllegalArgumentException("Member 의 필드로 허용된 길이를 넘은 column 을 넣을 수 없습니다.");
        }
    }

    private boolean overLength(String target, int maxLength) {
        if (target == null) {
            return false;
        }
        return target.length() > maxLength;
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
