package com.festago.member.domain;

import com.festago.auth.domain.SocialType;
import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.Validator;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.util.StringUtils;

@Entity
@SQLDelete(sql = "UPDATE member SET deleted_at = now(), nickname = '탈퇴한 회원', profile_image = '', social_id = null WHERE id=?")
@Where(clause = "deleted_at is null")
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "SOCIAL_UNIQUE",
            columnNames = {
                "socialId",
                "socialType"
            }
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    private static final String DEFAULT_IMAGE_URL = "https://festa-go.site/images/default-profile.png";
    private static final int MAX_SOCIAL_ID_LENGTH = 255;
    private static final int MAX_NICKNAME_LENGTH = 30;
    private static final int MAX_PROFILE_IMAGE_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = MAX_SOCIAL_ID_LENGTH)
    private String socialId;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @NotNull
    @Size(max = MAX_NICKNAME_LENGTH)
    private String nickname;

    @NotNull
    @Size(max = MAX_PROFILE_IMAGE_LENGTH)
    private String profileImage;

    private LocalDateTime deletedAt = null;

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
        this.profileImage = (StringUtils.hasText(profileImage)) ? profileImage : DEFAULT_IMAGE_URL;
    }

    private void validate(String socialId, SocialType socialType, String nickname, String profileImage) {
        validateSocialId(socialId);
        validateSocialType(socialType);
        validateNickname(nickname);
        validateProfileImage(profileImage);
    }

    private void validateSocialId(String socialId) {
        String fieldName = "socialId";
        Validator.notBlank(socialId, fieldName);
        Validator.maxLength(socialId, MAX_SOCIAL_ID_LENGTH, fieldName);
    }

    private void validateSocialType(SocialType socialType) {
        Validator.notNull(socialType, "socialType");
    }

    private void validateNickname(String nickname) {
        String fieldName = "nickname";
        Validator.notBlank(nickname, fieldName);
        Validator.maxLength(nickname, MAX_NICKNAME_LENGTH, fieldName);
    }

    private void validateProfileImage(String profileImage) {
        Validator.maxLength(profileImage, MAX_PROFILE_IMAGE_LENGTH, "profileImage");
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
