package com.festago.config;

import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

@Profile("local")
@Configuration
public class DemoDataInitConfig {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    MemberTicketRepository memberTicketRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Member member = memberRepository.save(new Member(null));

        LocalDateTime now = LocalDateTime.now();

        Stage futureStage = stageRepository.save(new Stage(null, "테코대학교 축제", now.plusYears(1)));
        Stage pastStage = stageRepository.save(new Stage(null, "우테대학교 축제", now.minusWeeks(1)));

        Ticket ticket = ticketRepository.save(new Ticket(null, pastStage, now));
        memberTicketRepository.save(new MemberTicket(member, ticket, 1));
    }
}
