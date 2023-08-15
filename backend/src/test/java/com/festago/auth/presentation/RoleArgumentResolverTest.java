package com.festago.auth.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.auth.domain.AuthenticateContext;
import com.festago.auth.domain.Role;
import com.festago.exception.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RoleArgumentResolverTest {

    AuthenticateContext authenticateContext;

    RoleArgumentResolver roleArgumentResolver;

    @BeforeEach
    void setUp() {
        authenticateContext = new AuthenticateContext();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ADMIN", "ANONYMOUS"})
    void Role이_Member일때_Member가_아니면_예외(Role role) {
        // given
        roleArgumentResolver = new RoleArgumentResolver(Role.MEMBER, authenticateContext);
        authenticateContext.setAuthenticate(1L, role);

        // when & then
        assertThatThrownBy(() -> roleArgumentResolver.resolveArgument(null, null, null, null))
            .isInstanceOf(ForbiddenException.class)
            .hasMessage("해당 권한이 없습니다.");
    }

    @Test
    void Role이_Member일때_Member이면_성공() throws Exception {
        // given
        roleArgumentResolver = new RoleArgumentResolver(Role.MEMBER, authenticateContext);
        authenticateContext.setAuthenticate(1L, Role.MEMBER);

        // when
        Long memberId = roleArgumentResolver.resolveArgument(null, null, null, null);

        // then
        assertThat(memberId).isEqualTo(authenticateContext.getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"MEMBER", "ANONYMOUS"})
    void Role이_Admin일때_Admin이_아니면_예외(Role role) {
        // given
        roleArgumentResolver = new RoleArgumentResolver(Role.ADMIN, authenticateContext);
        authenticateContext.setAuthenticate(1L, role);

        // when & then
        assertThatThrownBy(() -> roleArgumentResolver.resolveArgument(null, null, null, null))
            .isInstanceOf(ForbiddenException.class)
            .hasMessage("해당 권한이 없습니다.");
    }

    @Test
    void Role이_Admin일때_Admin이면_성공() throws Exception {
        // given
        roleArgumentResolver = new RoleArgumentResolver(Role.ADMIN, authenticateContext);
        authenticateContext.setAuthenticate(1L, Role.ADMIN);

        // when
        Long memberId = roleArgumentResolver.resolveArgument(null, null, null, null);

        // then
        assertThat(memberId).isEqualTo(authenticateContext.getId());
    }
}
