package com.festago.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.application.AdminService;
import com.festago.application.EntryService;
import com.festago.application.FestivalService;
import com.festago.application.MemberTicketService;
import com.festago.application.StageService;
import com.festago.application.TicketService;
import com.festago.auth.application.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = {
        AdminController.class,
        FestivalController.class,
        MemberTicketController.class,
        StaffMemberTicketController.class,
        StageController.class
    }
)
public abstract class ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdminService adminService;

    @MockBean
    EntryService entryService;

    @MockBean
    FestivalService festivalService;

    @MockBean
    MemberTicketService memberTicketService;

    @MockBean
    StageService stageService;

    @MockBean
    TicketService ticketService;

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;
}
