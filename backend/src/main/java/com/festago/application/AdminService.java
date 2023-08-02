package com.festago.application;

import com.festago.domain.Festival;
import com.festago.domain.FestivalRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketAmount;
import com.festago.domain.TicketEntryTime;
import com.festago.domain.TicketRepository;
import com.festago.domain.TicketType;
import java.time.LocalDate;
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

    public AdminService(FestivalRepository festivalRepository, StageRepository stageRepository,
                        TicketRepository ticketRepository) {
        this.festivalRepository = festivalRepository;
        this.stageRepository = stageRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public AdminResponse getAdminResponse() {
        List<Ticket> allTicket = ticketRepository.findAll();
        List<Stage> allStage = stageRepository.findAll();
        List<Festival> allFestival = festivalRepository.findAll();
        return new AdminResponse(
            ticketResponses(allTicket),
            stageResponses(allStage),
            festivalResponses(allFestival));
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

    public record AdminResponse(
        List<AdminTicketResponse> adminTickets,
        List<AdminStageResponse> adminStageResponse,
        List<AdminFestivalResponse> adminFestivalResponse) {

    }

    public record AdminFestivalResponse(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        String thumbnail) {

    }

    public record AdminStageResponse(
        Long id,
        Long festivalId,
        LocalDateTime startTime,
        String lineUp,
        List<Long> ticketId) {

    }

    public record AdminTicketResponse(
        Long id,
        Long stageId,
        TicketType ticketType,
        Integer totalAmount,
        Integer reservedAmount,
        Map<LocalDateTime, Integer> entryTimeAmount) {

    }
}
