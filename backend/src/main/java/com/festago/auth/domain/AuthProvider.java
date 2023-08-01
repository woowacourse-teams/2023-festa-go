package com.festago.auth.domain;

import com.festago.domain.Member;

public interface AuthProvider {

    String provide(Member member);
}
