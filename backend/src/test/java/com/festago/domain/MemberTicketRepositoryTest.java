package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTicketRepositoryTest {

    @Autowired
    MemberTicketRepository memberTicketRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    StageRepository stageRepository;

    @Test
    void 회원의_ID로_에매한_티켓을_모두_조회() {
        // given
        Member member1 = memberRepository.save(new Member());
        Member member2 = memberRepository.save(new Member());

        Stage stage1 = stageRepository.save(new Stage("테코대학교 축제", LocalDateTime.now()));
        Stage stage2 = stageRepository.save(new Stage("우테대학교 축제", LocalDateTime.now()));

        Ticket ticket1 = ticketRepository.save(new Ticket(null, stage1, LocalDateTime.now()));
        Ticket ticket2 = ticketRepository.save(new Ticket(null, stage2, LocalDateTime.now()));

        memberTicketRepository.save(new MemberTicket(member1, ticket1));
        memberTicketRepository.save(new MemberTicket(member1, ticket2));
        memberTicketRepository.save(new MemberTicket(member2, ticket1));

        // when
        List<MemberTicket> memberTickets = memberTicketRepository.findAllByOwnerId(member1.getId());

        // then
        assertThat(memberTickets).hasSize(2);
    }
}
