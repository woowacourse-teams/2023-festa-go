package com.festago.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.FestivalFixture;
import com.festago.support.MemberFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.support.StageFixture;
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

    @Test
    void 예매한_티켓_조회시_Pageable_적용() {
        // given
        Member member = memberRepository.save(MemberFixture.member().build());
        Festival festival = festivalRepository.save(FestivalFixture.festival().build());
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());
        for (int i = 0; i < 20; i++) {
            memberTicketRepository.save(MemberTicketFixture.memberTicket()
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
