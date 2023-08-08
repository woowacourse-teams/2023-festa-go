package com.festago.domain;

import com.festago.auth.domain.SocialType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE member SET deleted_at = now(), nickname = '탈퇴한 회원', profile_image = '' WHERE id=?")
@Where(clause = "deleted_at is null")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String nickname;

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
        this.profileImage = profileImage;
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
