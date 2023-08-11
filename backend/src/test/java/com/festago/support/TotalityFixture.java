package com.festago.support;

import com.festago.domain.Festival;
import com.festago.domain.Stage;
import com.festago.domain.Ticket;
import com.festago.domain.TicketType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public record TotalityFixture(
    Festival festival,
    Stage stage,
    Ticket ticket
) {

    public static class 픽스쳐를 {

        private final 축제의 축제의 = new 축제의(this);
        private final 공연의 공연의 = new 공연의(this);
        private final 티켓의 티켓의 = new 티켓의(this);

        public static 픽스쳐를 생성한다() {
            return new 픽스쳐를();
        }

        public TotalityFixture 이다() {
            Festival festival = FestivalFixture.festival()
                .id(축제의.id)
                .startDate(축제의.startDate != null ? 축제의.startDate : 공연의.startTime.toLocalDate())
                .endDate(축제의.endDate != null ? 축제의.endDate : 공연의.startTime.toLocalDate())
                .build();
            Stage stage = StageFixture.stage()
                .id(공연의.id)
                .festival(festival)
                .startTime(공연의.startTime != null ? 공연의.startTime : 축제의.startDate.atTime(18, 0))
                .ticketOpenTime(공연의.ticketOpenTime != null ? 공연의.ticketOpenTime : 축제의.startDate.atTime(12, 0))
                .build();
            Ticket ticket = TicketFixture.ticket()
                .id(티켓의.id)
                .stage(stage)
                .ticketType(티켓의.ticketType != null ? 티켓의.ticketType : TicketType.VISITOR)
                .build();
            return new TotalityFixture(
                festival,
                stage,
                ticket
            );
        }

        public 축제의 축제의() {
            return 축제의;
        }

        public 공연의 공연의() {
            return 공연의;
        }

        public 티켓의 티켓의() {
            return 티켓의;
        }

        private abstract static class 픽스쳐 {

            protected Long id;

            private final 픽스쳐를 픽스쳐를;

            public 픽스쳐(픽스쳐를 픽스쳐를) {
                this.픽스쳐를 = 픽스쳐를;
            }

            public final 픽스쳐를 그리고() {
                return 픽스쳐를;
            }

            public final TotalityFixture 이다() {
                return 픽스쳐를.이다();
            }
        }

        public static class 축제의 extends 픽스쳐 {

            private LocalDate startDate;
            private LocalDate endDate;

            public 축제의(픽스쳐를 픽스쳐를) {
                super(픽스쳐를);
            }

            public 축제의 Id는(Long id) {
                this.id = id;
                return this;
            }

            public 축제의 시작일은(LocalDate startDate) {
                this.startDate = startDate;
                return this;
            }

            public 축제의 종료일은(LocalDate endDate) {
                this.endDate = endDate;
                return this;
            }
        }

        public static class 공연의 extends 픽스쳐 {

            private LocalDateTime startTime;
            private LocalDateTime ticketOpenTime;

            public 공연의(TotalityFixture.픽스쳐를 픽스쳐를) {
                super(픽스쳐를);
            }

            public 공연의 Id는(Long id) {
                this.id = id;
                return this;
            }

            public 공연의 시작시간은(LocalDateTime startTime) {
                this.startTime = startTime;
                return this;
            }

            public 공연의 예매오픈시간은(LocalDateTime ticketOpenTime) {
                this.ticketOpenTime = ticketOpenTime;
                return this;
            }
        }

        public static class 티켓의 extends 픽스쳐 {

            private TicketType ticketType;

            public 티켓의(픽스쳐를 픽스쳐를) {
                super(픽스쳐를);
            }

            public 티켓의 Id는(Long id) {
                this.id = id;
                return this;
            }

            public 티켓의 타입은(TicketType ticketType) {
                this.ticketType = ticketType;
                return this;
            }
        }
    }
}
