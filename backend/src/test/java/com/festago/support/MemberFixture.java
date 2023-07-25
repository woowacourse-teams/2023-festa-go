package com.festago.support;

import com.festago.domain.Member;

public class MemberFixture {

    private Long id = 1L;

    public static MemberFixture member() {
        return new MemberFixture();
    }

    public MemberFixture id(Long id) {
        this.id = id;
        return this;
    }

    public Member build() {
        return new Member(this.id);
    }
}
