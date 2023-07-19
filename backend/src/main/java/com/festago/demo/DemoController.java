package com.festago.demo;

import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketRepository;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    private final MemberRepository memberRepository;
    private final StageRepository stageRepository;
    private final TicketRepository ticketRepository;
    private final MemberTicketRepository memberTicketRepository;

    public DemoController(MemberRepository memberRepository, StageRepository stageRepository,
                          TicketRepository ticketRepository, MemberTicketRepository memberTicketRepository) {
        this.memberRepository = memberRepository;
        this.stageRepository = stageRepository;
        this.ticketRepository = ticketRepository;
        this.memberTicketRepository = memberTicketRepository;
    }

    @PostMapping("/tickets")
    public ResponseEntity<CreateTicketResponse> createTicket(@RequestBody CreateTicketRequest request) {
        Stage stage = stageRepository.findById(1L).get();
        Ticket ticket = ticketRepository.save(new Ticket(stage, request.entryTime));
        Member member = memberRepository.findById(1L).get();
        MemberTicket memberTicket = memberTicketRepository.save(new MemberTicket(member, ticket));
        CreateTicketResponse response = new CreateTicketResponse(ticket.getId(), memberTicket.getId());
        return ResponseEntity.ok().body(response);
    }

    record CreateTicketRequest(LocalDateTime entryTime) {

    }

    record CreateTicketResponse(Long ticketId, Long memberTicketId) {

    }
}

