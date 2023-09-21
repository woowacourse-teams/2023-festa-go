package com.festago.admin.application;

import com.festago.admin.dto.AdminFestivalResponse;
import com.festago.admin.dto.AdminResponse;
import com.festago.admin.dto.AdminSchoolResponse;
import com.festago.admin.dto.AdminStageResponse;
import com.festago.admin.dto.AdminTicketResponse;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketAmount;
import com.festago.ticket.domain.TicketEntryTime;
import com.festago.ticket.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AdminService {

    private final FestivalRepository festivalRepository;
    private final StageRepository stageRepository;
    private final TicketRepository ticketRepository;
    private final SchoolRepository schoolRepository;

    public AdminService(FestivalRepository festivalRepository, StageRepository stageRepository,
                        TicketRepository ticketRepository,
                        SchoolRepository schoolRepository) {
        this.festivalRepository = festivalRepository;
        this.stageRepository = stageRepository;
        this.ticketRepository = ticketRepository;
        this.schoolRepository = schoolRepository;
    }

    @Transactional(readOnly = true)
    public AdminResponse getAdminResponse() {
        List<School> allSchool = schoolRepository.findAll();
        List<Ticket> allTicket = ticketRepository.findAll();
        List<Stage> allStage = stageRepository.findAll();
        List<Festival> allFestival = festivalRepository.findAll();
        return new AdminResponse(
            schoolResponses(allSchool),
            ticketResponses(allTicket),
            stageResponses(allStage),
            festivalResponses(allFestival));
    }

    private List<AdminSchoolResponse> schoolResponses(List<School> schools) {
        return schools.stream()
            .map(AdminSchoolResponse::from)
            .toList();
    }

    private List<AdminTicketResponse> ticketResponses(List<Ticket> tickets) {
        return tickets.stream()
            .map(this::ticketResponse)
            .toList();
    }

    private AdminTicketResponse ticketResponse(Ticket ticket) {
        TicketAmount ticketAmount = ticket.getTicketAmount();
        Map<LocalDateTime, Integer> amountByEntryTime = ticket.getTicketEntryTimes().stream()
            .collect(Collectors.toMap(
                TicketEntryTime::getEntryTime,
                TicketEntryTime::getAmount
            ));
        return new AdminTicketResponse(
            ticket.getId(),
            ticket.getStage().getId(),
            ticket.getTicketType(),
            ticketAmount.getTotalAmount(),
            ticketAmount.getReservedAmount(),
            amountByEntryTime
        );
    }

    private List<AdminStageResponse> stageResponses(List<Stage> stages) {
        return stages.stream()
            .map(this::stageResponse)
            .toList();
    }

    private AdminStageResponse stageResponse(Stage stage) {
        List<Long> ticketIds = stage.getTickets().stream()
            .map(Ticket::getId)
            .toList();
        return new AdminStageResponse(
            stage.getId(),
            stage.getFestival().getId(),
            stage.getStartTime(),
            stage.getLineUp(),
            ticketIds
        );
    }

    private List<AdminFestivalResponse> festivalResponses(List<Festival> festivals) {
        return festivals.stream()
            .map(festival -> new AdminFestivalResponse(
                festival.getId(),
                festival.getName(),
                festival.getStartDate(),
                festival.getEndDate(),
                festival.getThumbnail()))
            .toList();
    }
}
