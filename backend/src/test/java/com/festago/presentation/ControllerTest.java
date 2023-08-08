package com.festago.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.application.AdminService;
import com.festago.application.EntryService;
import com.festago.application.FestivalService;
import com.festago.application.MemberTicketService;
import com.festago.application.StageService;
import com.festago.application.TicketService;
import com.festago.auth.application.AuthService;
import com.festago.auth.domain.AuthExtractor;
import com.festago.auth.presentation.AuthController;
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
        StageController.class,
        AuthController.class,
    }
)
public abstract class ControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public AuthExtractor authExtractor;

    @MockBean
    public AdminService adminService;

    @MockBean
    public EntryService entryService;

    @MockBean
    public FestivalService festivalService;

    @MockBean
    public MemberTicketService memberTicketService;

    @MockBean
    public StageService stageService;

    @MockBean
    public TicketService ticketService;

    @MockBean
    public AuthService authService;

    @Autowired
    public ObjectMapper objectMapper;
}
