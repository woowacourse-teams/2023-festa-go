package com.festago.application;

import com.festago.dto.StageTicketResponse;
import com.festago.dto.StageTicketsResponse;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketReadService {

    private static final int SOLD_OUT = 0;
    private final RedisTemplate<String, String> redisTemplate;
    private final TicketService ticketService;

    public TicketReadService(RedisTemplate<String, String> redisTemplate, TicketService ticketService) {
        this.redisTemplate = redisTemplate;
        this.ticketService = ticketService;
    }

    public StageTicketsResponse findStageTickets(Long stageId) {
        StageTicketsResponse stageTickets = ticketService.findStageTickets(stageId);
        if (hasRedis(stageTickets)) {
            return stageTickets.tickets().stream()
                .map(this::setRemainFromRedis)
                .collect(Collectors.collectingAndThen(
                    Collectors.toList(),
                    StageTicketsResponse::new));
        }

        return stageTickets;
    }

    private boolean hasRedis(StageTicketsResponse stageTickets) {
        Long ticketId = stageTickets.tickets().get(0).id();
        Boolean result = redisTemplate.hasKey("ticketAmount_" + ticketId.toString());
        return Objects.equals(Boolean.TRUE, result);
    }

    private StageTicketResponse setRemainFromRedis(StageTicketResponse stageTicketResponse) {
        String value = redisTemplate.opsForValue().get("ticketAmount_" + stageTicketResponse.id().toString());
        int remainAmount = Math.max(Integer.parseInt(value), SOLD_OUT);
        return stageTicketResponse.setRemainAmount(remainAmount);
    }
}
