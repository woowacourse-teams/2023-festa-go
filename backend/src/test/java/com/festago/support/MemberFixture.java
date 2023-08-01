package com.festago.support;

import com.festago.auth.domain.SocialType;
import com.festago.domain.Member;

public class MemberFixture {

    private Long id;
    private String socialId = "123";
    private SocialType socialType = SocialType.KAKAO;
    private String nickname = "nickname";
    private String profileImage = "https://profileImage.com/image.png";

    public static MemberFixture member() {
        return new MemberFixture();
    }

    public MemberFixture id(Long id) {
        this.id = id;
        return this;
    }

    public MemberFixture socialId(String socialId) {
        this.socialId = socialId;
        return this;
    }

    public MemberFixture socialType(SocialType socialType) {
        this.socialType = socialType;
        return this;
    }

    public MemberFixture nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public MemberFixture profileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public Member build() {
        return new Member(this.id, this.socialId, this.socialType, this.nickname, this.profileImage);
    }
}
