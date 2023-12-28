package com.festago.stage.application;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.application.FestivalStageService;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.DetailFestivalResponse;
import com.festago.festival.dto.DetailStageResponse;
import com.festago.festival.dto.DetailTicketResponse;
import com.festago.festival.repository.FestivalRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketAmount;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalStageServiceImpl implements FestivalStageService {

    private final StageRepository stageRepository;
    private final FestivalRepository festivalRepository;

    @Transactional(readOnly = true)
    @Override
    public DetailFestivalResponse findDetail(Long festivalId) {
        Festival festival = findFestival(festivalId);
        return stageRepository.findAllDetailByFestivalId(festivalId).stream()
            .sorted(comparing(Stage::getStartTime))
            .map(this::makeResponse)
            .collect(collectingAndThen(
                toList(),
                stageResponses -> new DetailFestivalResponse(
                    festivalId,
                    festival.getSchool().getId(),
                    festival.getName(),
                    festival.getStartDate(),
                    festival.getEndDate(),
                    festival.getThumbnail(),
                    stageResponses
                )
            ));
    }

    private Festival findFestival(Long festivalId) {
        return festivalRepository.findById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
    }

    private DetailStageResponse makeResponse(Stage stage) {
        return stage.getTickets().stream()
            .map(makeTicketResponse())
            .collect(collectingAndThen(
                toList(),
                ticketResponses -> new DetailStageResponse(
                    stage.getId(),
                    stage.getStartTime(),
                    stage.getTicketOpenTime(),
                    stage.getLineUp(),
                    ticketResponses
                )
            ));
    }

    private Function<Ticket, DetailTicketResponse> makeTicketResponse() {
        return ticket -> {
            TicketAmount ticketAmount = ticket.getTicketAmount();
            return new DetailTicketResponse(
                ticket.getId(),
                ticket.getTicketType().name(),
                ticketAmount.getTotalAmount(),
                ticketAmount.calculateRemainAmount()
            );
        };
    }
}
