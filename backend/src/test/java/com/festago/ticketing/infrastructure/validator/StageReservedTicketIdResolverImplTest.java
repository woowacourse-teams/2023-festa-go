package com.festago.ticketing.infrastructure.validator;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.support.fixture.StageTicketFixture;
import com.festago.ticket.domain.StageTicket;
import com.festago.ticket.repository.StageTicketRepository;
import com.festago.ticketing.domain.Booker;
import com.festago.ticketing.repository.ReserveTicketRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageReservedTicketIdResolverImplTest extends ApplicationIntegrationTest {

    @Autowired
    StageReservedTicketIdResolverImpl stageReservedTicketIdResolver;

    @Autowired
    ReserveTicketRepository reserveTicketRepository;

    @Autowired
    StageTicketRepository stageTicketRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    School school;

    Stage stage;

    StageTicket stageTicket;

    @BeforeEach
    void setUp() {
        school = schoolRepository.save(SchoolFixture.builder().build());
        Festival festival = festivalRepository.save(FestivalFixture.builder().school(school).build());
        stage = stageRepository.save(StageFixture.builder().festival(festival).build());
        stageTicket = stageTicketRepository.save(
            StageTicketFixture.builder().schoolId(school.getId()).stage(stage).build());
        LocalDateTime now = stage.getTicketOpenTime().minusHours(1);
        LocalDateTime entryTime = stage.getStartTime().minusHours(1);
        stageTicket.addTicketEntryTime(school.getId(), now, entryTime, 100);
    }

    @Test
    void 사용자가_예매한_티켓이_없으면_null이_반환된다() {
        // when
        Long ticketId = stageReservedTicketIdResolver.resolve(1L, stage.getId());

        // then
        assertThat(ticketId).isNull();
    }

    @Test
    void 사용자가_공연의_티켓에_예매한_티켓이_있으면_해당_티켓의_식별자가_반환된다() {
        // given
        Booker booker = new Booker(1L, school.getId());
        reserveTicketRepository.save(stageTicket.reserve(booker, 1));

        // when
        Long ticketId = stageReservedTicketIdResolver.resolve(1L, stage.getId());

        // then
        assertThat(ticketId).isEqualTo(stageTicket.getId());
    }
}
