package com.festago.entryalert.domain;

import static com.festago.common.exception.ErrorCode.NOT_PENDING_ALERT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.support.EntryAlertFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EntryAlertTest {

    @Nested
    class 요청처리 {

        @ValueSource(strings = {"REQUESTED", "SENT", "FAILED"})
        @ParameterizedTest
        void 대기상태가_아니면_예외(AlertStatus status) {
            // given
            EntryAlert entryAlert = EntryAlertFixture.entryAlert().status(status).build();

            // when & then
            assertThatThrownBy(entryAlert::request)
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_PENDING_ALERT.getMessage());
        }

        @Test
        void 요청_상태로_변경() {
            // given
            EntryAlert entryAlert = EntryAlertFixture.entryAlert().status(AlertStatus.PENDING).build();

            // when
            entryAlert.request();

            // then
            assertThat(entryAlert.getStatus()).isEqualTo(AlertStatus.REQUESTED);
        }
    }

    @Nested
    class 전송처리 {

        @ValueSource(strings = {"PENDING", "SENT", "FAILED"})
        @ParameterizedTest
        void 요청상태가_아니면_예외(AlertStatus status) {
            // given
            EntryAlert entryAlert = EntryAlertFixture.entryAlert().status(status).build();

            // when & then
            assertThatThrownBy(entryAlert::send)
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_PENDING_ALERT.getMessage());
        }

        @Test
        void 전송_상태로_변경() {
            // given
            EntryAlert entryAlert = EntryAlertFixture.entryAlert().status(AlertStatus.REQUESTED).build();

            // when
            entryAlert.send();

            // then
            assertThat(entryAlert.getStatus()).isEqualTo(AlertStatus.SENT);
        }
    }

    @Nested
    class 실패처리 {

        @ValueSource(strings = {"PENDING", "SENT", "FAILED"})
        @ParameterizedTest
        void 요청상태가_아니면_예외(AlertStatus status) {
            // given
            EntryAlert entryAlert = EntryAlertFixture.entryAlert().status(status).build();

            // when & then
            assertThatThrownBy(entryAlert::fail)
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_PENDING_ALERT.getMessage());
        }

        @Test
        void 전송_상태로_변경() {
            // given
            EntryAlert entryAlert = EntryAlertFixture.entryAlert().status(AlertStatus.REQUESTED).build();

            // when
            entryAlert.fail();

            // then
            assertThat(entryAlert.getStatus()).isEqualTo(AlertStatus.FAILED);
        }
    }
}
