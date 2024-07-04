package com.festago.auth.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.auth.domain.Role;
import com.festago.auth.domain.authentication.Authentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberAuthenticationClaimsExtractorTest {

    MemberAuthenticationClaimsExtractor memberAuthenticationClaimsExtractor = new MemberAuthenticationClaimsExtractor();

    @ParameterizedTest
    @EnumSource(names = "MEMBER", mode = EnumSource.Mode.EXCLUDE)
    void Claims의_audience가_MEMBER가_아니면_반환되는_Authentication의_권한은_ANONYMOUS이다(Role role) {
        // given
        Claims claims = Jwts.claims()
            .audience().add(role.name()).and()
            .build();

        // when
        Authentication authentication = memberAuthenticationClaimsExtractor.extract(claims);

        // then
        assertThat(authentication.getRole())
            .isEqualTo(Role.ANONYMOUS);
    }

    @Test
    void Claims의_audience가_MEMBER이면_Authentication의_권한은_MEMBER이다() {
        // given
        Claims claims = Jwts.claims()
            .audience().add(Role.MEMBER.name()).and()
            .subject("1")
            .build();

        // when
        Authentication authentication = memberAuthenticationClaimsExtractor.extract(claims);

        // then
        assertThat(authentication.getRole())
            .isEqualTo(Role.MEMBER);
    }
}
