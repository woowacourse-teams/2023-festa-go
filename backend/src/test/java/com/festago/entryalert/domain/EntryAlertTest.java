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

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EntryAlertTest {

    @Nested
    class 요청처리 {

        @Test
        void 대기상태가_아니면_예외() {
            // given
            EntryAlert entryAlert = EntryAlertFixture.entryAlert().status(AlertStatus.REQUESTED).build();

            // when & then
            assertThatThrownBy(entryAlert::changeRequested)
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_PENDING_ALERT.getMessage());
        }

        @Test
        void 요청_상태로_변경() {
            // given
            EntryAlert entryAlert = EntryAlertFixture.entryAlert().status(AlertStatus.PENDING).build();

            // when
            entryAlert.changeRequested();

            // then
            assertThat(entryAlert.getStatus()).isEqualTo(AlertStatus.REQUESTED);
        }
    }
}
