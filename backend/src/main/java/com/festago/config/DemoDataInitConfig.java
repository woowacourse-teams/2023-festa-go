package com.festago.config;

import com.festago.domain.Festival;
import com.festago.domain.FestivalRepository;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketRepository;
import com.festago.domain.TicketType;
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

    @Autowired
    FestivalRepository festivalRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Member member = memberRepository.save(new Member());

        LocalDateTime now = LocalDateTime.now();

        Festival festival = festivalRepository.save(
            new Festival("테코 대학교 축제", now.toLocalDate(), now.plusDays(3L).toLocalDate()));

        String lineUp = "오리, 푸우, 글렌, 애쉬";
        LocalDateTime ticketOpenTime = now.minusWeeks(2);
        stageRepository.save(new Stage(now.plusYears(1), lineUp, ticketOpenTime, festival));
        Stage pastStage = stageRepository.save(new Stage(now.minusWeeks(1), lineUp, ticketOpenTime, festival));

        Ticket ticket = ticketRepository.save(new Ticket(pastStage, TicketType.VISITOR, 100, now));
        memberTicketRepository.save(new MemberTicket(member, ticket, 1, LocalDateTime.now()));
    }
}
