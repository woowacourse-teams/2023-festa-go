package com.festago.application;

import com.festago.domain.Member;
import com.festago.domain.Ticket;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class MemoryTicketAmountService implements TicketAmountService {

    private static final int MAX_MEMBER_TRIAL_COUNT = 1;

    private final Map<String, AtomicInteger> memberTrialCountMap = new ConcurrentHashMap<>();
    private final Map<Long, AtomicInteger> ticketAmountMap = new ConcurrentHashMap<>();

    @Override
    public Optional<Integer> getSequence(Ticket ticket, Member member) {
        String memberKey = makeMemberKey(ticket, member);
        int trialCount = memberTrialCountMap.computeIfAbsent(memberKey, ignore -> new AtomicInteger())
            .incrementAndGet();
        if (trialCount > MAX_MEMBER_TRIAL_COUNT) {
            return Optional.empty();
        }
        Integer totalAmount = ticket.getTicketAmount().getTotalAmount();
        int quantity = ticketAmountMap.computeIfAbsent(ticket.getId(), ignore -> new AtomicInteger(totalAmount))
            .decrementAndGet();
        if (quantity < 0) {
            return Optional.empty();
        }
        return Optional.of(totalAmount - quantity);
    }

    private String makeMemberKey(Ticket ticket, Member member) {
        Long stageId = ticket.getStage().getId();
        Long memberId = member.getId();
        return String.format("trialCount_%d_%d", stageId, memberId);
    }

    public void clear() {
        ticketAmountMap.clear();
        memberTrialCountMap.clear();
    }
}
