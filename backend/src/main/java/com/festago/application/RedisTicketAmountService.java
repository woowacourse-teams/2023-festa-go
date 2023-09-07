package com.festago.application;

import com.festago.domain.Member;
import com.festago.domain.Ticket;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

@Profile("!test")
@Service
public class RedisTicketAmountService implements TicketAmountService {

    private static final String MAX_MEMBER_TRIAL_COUNT = "1";
    
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisScript<Long> redisScript;

    public RedisTicketAmountService(RedisTemplate<String, String> redisTemplate, RedisScript<Long> redisScript) {
        this.redisTemplate = redisTemplate;
        this.redisScript = redisScript;
    }

    @Override
    public Optional<Integer> getSequence(Ticket ticket, Member member) {
        Long remainAmount = redisTemplate.execute(
            redisScript,
            List.of(makeMemberKey(ticket, member), makeTicketAmountKey(ticket)),
            MAX_MEMBER_TRIAL_COUNT);
        if (remainAmount == null || remainAmount < 0) {
            return Optional.empty();
        }
        Integer totalAmount = ticket.getTicketAmount().getTotalAmount();
        return Optional.of(totalAmount - remainAmount.intValue());
    }

    private String makeMemberKey(Ticket ticket, Member member) {
        Long stageId = ticket.getStage().getId();
        Long memberId = member.getId();
        return String.format("trialCount_%d_%d", stageId, memberId);
    }

    private String makeTicketAmountKey(Ticket ticket) {
        return String.format("ticketAmount_%d", ticket.getId());
    }
}
