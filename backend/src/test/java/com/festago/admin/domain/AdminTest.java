package com.festago.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.festago.common.exception.ValidException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminTest {

    @Test
    void 어드민_생성_성공() {
        // given
        Admin admin = new Admin("admin", "password");

        // when & then
        assertThat(admin.getUsername()).isEqualTo("admin");
        assertThat(admin.getPassword()).isEqualTo("password");
    }

    @Test
    void username이_4글자_미만이면_예외() {
        // given
        String username = "1".repeat(3);

        // when & then
        assertThatThrownBy(() -> new Admin(username, "password"))
            .isInstanceOf(ValidException.class);
    }

    @Test
    void username이_20글자_초과하면_예외() {
        // given
        String username = "1".repeat(21);

        // when & then
        assertThatThrownBy(() -> new Admin(username, "password"))
            .isInstanceOf(ValidException.class);
    }

    @Test
    void password가_4글자_미만이면_예외() {
        // given
        String password = "1".repeat(3);

        // when & then
        assertThatThrownBy(() -> new Admin("admin", password))
            .isInstanceOf(ValidException.class);
    }

    @Test
    void password가_20글자_초과하면_예외() {
        // given
        String password = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new Admin("admin", password))
            .isInstanceOf(ValidException.class);
    }
}
