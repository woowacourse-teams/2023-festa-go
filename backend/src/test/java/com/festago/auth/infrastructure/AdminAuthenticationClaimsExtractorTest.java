package com.festago.auth.infrastructure;

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
class AdminAuthenticationClaimsExtractorTest {

    AdminAuthenticationClaimsExtractor adminAuthenticationClaimsExtractor = new AdminAuthenticationClaimsExtractor();

    @ParameterizedTest
    @EnumSource(names = "ADMIN", mode = EnumSource.Mode.EXCLUDE)
    void Claims의_audience가_ADMIN이_아니면_반환되는_Authentication의_권한은_ANONYMOUS이다(Role role) {
        // given
        Claims claims = Jwts.claims()
            .audience().add(role.name()).and()
            .build();

        // when
        Authentication authentication = adminAuthenticationClaimsExtractor.extract(claims);

        // then
        assertThat(authentication.getRole())
            .isEqualTo(Role.ANONYMOUS);
    }

    @Test
    void Claims의_audience가_ADMIN이면_Authentication의_권한은_ADMIN이다() {
        // given
        Claims claims = Jwts.claims()
            .audience().add(Role.ADMIN.name()).and()
            .add("adminId", 1)
            .build();

        // when
        Authentication authentication = adminAuthenticationClaimsExtractor.extract(claims);

        // then
        assertThat(authentication.getRole())
            .isEqualTo(Role.ADMIN);
    }
}
