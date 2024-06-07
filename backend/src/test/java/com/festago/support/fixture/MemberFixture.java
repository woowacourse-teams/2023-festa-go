package com.festago.support.fixture;

import com.festago.member.domain.Member;
import com.festago.member.domain.SocialType;

public class MemberFixture extends BaseFixture {

    private Long id;
    private String socialId;
    private SocialType socialType = SocialType.FESTAGO;
    private String nickname;
    private String profileImageUrl = "https://image.com/profileImage.png";

    public static MemberFixture builder() {
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

    public MemberFixture profileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return this;
    }

    public Member build() {
        return new Member(
            id,
            uniqueValue("", socialId),
            socialType,
            uniqueValue("nickname", nickname),
            profileImageUrl
        );
    }
}
