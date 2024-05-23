package com.festago.ticketing.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.MemberFixture;
import com.festago.support.fixture.MemberTicketFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.ticket.repository.TicketRepository;
import com.festago.ticketing.application.MemberTicketService;
import com.festago.ticketing.dto.MemberTicketsResponse;
import com.festago.ticketing.repository.MemberTicketRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTicketIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    MemberTicketService memberTicketService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberTicketRepository memberTicketRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Test
    void 예매한_티켓_조회시_Pageable_적용() {
        // given
        School school = schoolRepository.save(SchoolFixture.builder().build());
        Member member = memberRepository.save(MemberFixture.builder().build());
        Festival festival = festivalRepository.save(FestivalFixture.builder().school(school).build());
        Stage stage = stageRepository.save(StageFixture.builder().festival(festival).build());
        for (int i = 0; i < 20; i++) {
            memberTicketRepository.save(MemberTicketFixture.builder()
                .stage(stage)
                .owner(member)
                .build()
            );
        }

        // when
        MemberTicketsResponse actual = memberTicketService.findAll(member.getId(), PageRequest.of(0, 10));

        // then
        assertThat(actual.memberTickets()).hasSize(10);
    }
}
