package com.festago.support;

import com.festago.auth.application.AdminAuthService;
import com.festago.auth.application.AuthFacadeService;
import com.festago.entry.application.EntryService;
import com.festago.fcm.application.MemberFCMService;
import com.festago.festival.application.FestivalService;
import com.festago.member.application.MemberService;
import com.festago.school.application.SchoolService;
import com.festago.stage.application.StageService;
import com.festago.student.application.StudentService;
import com.festago.ticket.application.TicketService;
import com.festago.ticketing.application.MemberTicketService;
import com.festago.ticketing.application.TicketingService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockAllServiceConfig {

    @Bean
    public FestivalService festivalService() {
        return Mockito.mock(FestivalService.class);
    }

    @Bean
    public StageService stageService() {
        return Mockito.mock(StageService.class);
    }

    @Bean
    public TicketService ticketService() {
        return Mockito.mock(TicketService.class);
    }

    @Bean
    public AdminAuthService adminAuthService() {
        return Mockito.mock(AdminAuthService.class);
    }

    @Bean
    public SchoolService schoolService() {
        return Mockito.mock(SchoolService.class);
    }

    @Bean
    public MemberTicketService memberTicketService() {
        return Mockito.mock(MemberTicketService.class);
    }

    @Bean
    public EntryService entryService() {
        return Mockito.mock(EntryService.class);
    }

    @Bean
    public TicketingService ticketingService() {
        return Mockito.mock(TicketingService.class);
    }

    @Bean
    public AuthFacadeService authFacadeService() {
        return Mockito.mock(AuthFacadeService.class);
    }

    @Bean
    public MemberFCMService memberFCMService() {
        return Mockito.mock(MemberFCMService.class);
    }

    @Bean
    public MemberService memberService() {
        return Mockito.mock(MemberService.class);
    }

    @Bean
    public StudentService studentService() {
        return Mockito.mock(StudentService.class);
    }
}
