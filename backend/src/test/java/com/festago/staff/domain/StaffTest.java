package com.festago.staff.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.stage.domain.Stage;
import com.festago.support.FestivalFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.support.StaffFixture;
import com.festago.support.StageFixture;
import com.festago.ticketing.domain.MemberTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StaffTest {

    @Nested
    class 티켓_검사_권한_확인 {

        MemberTicket memberTicket;
        Festival festival;

        @BeforeEach
        void setUp() {
            festival = FestivalFixture.festival().id(1L).build();
            Stage stage = StageFixture.stage().festival(festival).build();
            memberTicket = MemberTicketFixture.memberTicket().stage(stage).build();
        }

        @Test
        void 같은_축제이면_참() {
            // given
            Staff staff = StaffFixture.staff().festival(festival).build();

            // when
            boolean result = staff.canValidate(memberTicket);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 다른_축제면_거짓() {
            // given
            Festival otherFestival = FestivalFixture.festival().id(2L).build();
            Staff staff = StaffFixture.staff().festival(otherFestival).build();

            // when
            boolean result = staff.canValidate(memberTicket);

            // then
            assertThat(result).isFalse();
        }
    }
}
