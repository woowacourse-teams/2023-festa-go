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
        return AdminResponse.of(allTicket, allStage, allFestival);
    }

    public record AdminResponse(
        List<AdminTicketResponse> adminTickets,
        List<AdminStageResponse> adminStageResponse,
        List<AdminFestivalResponse> adminFestivalResponse) {

        public static AdminResponse of(List<Ticket> allTicket, List<Stage> allStage, List<Festival> allFestival) {
            List<AdminTicketResponse> allTicketResponse = allTicket.stream()
                .map(AdminTicketResponse::from)
                .toList();

            List<AdminStageResponse> allStageResponse = allStage.stream()
                .map(AdminStageResponse::from)
                .toList();

            List<AdminFestivalResponse> allFestivalResponse = allFestival.stream()
                .map(AdminFestivalResponse::from)
                .toList();

            return new AdminResponse(
                allTicketResponse,
                allStageResponse,
                allFestivalResponse
            );
        }


        public record AdminTicketResponse(
            Long id,
            Long stageId,
            TicketType ticketType,
            Integer totalAmount,
            Integer reservedAmount,
            Map<LocalDateTime, Integer> entryTimeAmount) {

            public static AdminTicketResponse from(Ticket ticket) {
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
        }

        public record AdminStageResponse(
            Long id,
            Long festivalId,
            LocalDateTime startTime,
            String lineUp,
            List<Long> ticketId
        ) {

            public static AdminStageResponse from(Stage stage) {
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
        }

        public record AdminFestivalResponse(
            Long id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            String thumbnail) {

            public static AdminFestivalResponse from(Festival festival) {
                return new AdminFestivalResponse(
                    festival.getId(),
                    festival.getName(),
                    festival.getStartDate(),
                    festival.getEndDate(),
                    festival.getThumbnail()
                );
            }
        }
    }
}
